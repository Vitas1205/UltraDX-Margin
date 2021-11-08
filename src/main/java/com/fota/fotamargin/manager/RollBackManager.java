package com.fota.fotamargin.manager;

import com.alibaba.dubbo.rpc.RpcContext;
import com.fota.common.Result;
import com.fota.fotamargin.common.enums.DeliveryEffectTypeEnum;
import com.fota.fotamargin.task.delivery.DeliveryRunnable;
import com.fota.margin.domain.ResultCode;
import com.fota.margin.domain.RollBackReqDto;
import com.fota.policy.domain.OperationAllLimitDTO;
import com.fota.policy.domain.enums.LimitTypeEnum;
import com.fota.policy.service.OperationLimitService;
import com.fota.trade.domain.ContractCategoryDTO;
import com.fota.trade.domain.RollbackResponse;
import com.fota.trade.domain.enums.ContractStatus;
import com.fota.trade.service.ContractCategoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author taoyuanming
 * Created on 2018/8/8
 * Description 合约回滚
 */
@Component
public class RollBackManager {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ContractCategoryService contractCategoryService;

    @Autowired
    private OperationLimitService operationLimitService;

    /**
     * 回滚合约
     * @param rollBackReqDto
     */
    public ResultCode rollBackContract(RollBackReqDto rollBackReqDto) {
        long rollbackTime = rollBackReqDto.getRollbackTime();
        //冻结所有用户提笔操作
        OperationAllLimitDTO operationAllLimitDTO = new OperationAllLimitDTO();
        operationAllLimitDTO.setLimitType(LimitTypeEnum.WITHDRAW_FREEZE.getCode());
        operationAllLimitDTO.setNote("回滚");
        try {
            operationLimitService.saveOperationAllLimit(operationAllLimitDTO);
        } catch (Exception e) {
            log.error("operationLimitService.saveOperationAllLimit Exception", e);
            //不继续回滚
            return ResultCode.error(1,"冻结失败");
        }


        //获得回滚合约
        int i = 0;
        List<ContractCategoryDTO> contractCategoryDTOList;
        do {
            // TODO: 2018/8/14  获取回滚中的合约优化
            contractCategoryDTOList = contractCategoryService.listActiveContract();
            i++;
        } while (CollectionUtils.isEmpty(contractCategoryDTOList) && i <= 2);
        if (CollectionUtils.isEmpty(contractCategoryDTOList)) {
            log.error("RollBackContract contractCategoryDTOList empty!");
            return ResultCode.error(2,"合约为空");
        }

        // TODO: 2018/8/8  控制回滚时间
        log.info("RollBackContract contractCategoryDTOList: {}", contractCategoryDTOList);
        Result<RollbackResponse> result;
        List<Future<Result<RollbackResponse>>> futureList = new ArrayList<>();
        Date date = new Date(rollBackReqDto.getRollbackTime());
        for (ContractCategoryDTO contractCategoryDTO : contractCategoryDTOList) {
            // 更新合约该合约为回滚中
            contractCategoryService.updateContractStatus(contractCategoryDTO.getId(), ContractStatus.ROLLBACK);
            // 根据合约id回滚该合约所有相关内容接口
            result = contractCategoryService.rollback(date, contractCategoryDTO.getId());
            futureList.add(RpcContext.getContext().getFuture());
        }

        List<Long> contractIdList = new ArrayList<>();
        RollbackResponse rollbackResponse;
        for (Future<Result<RollbackResponse>> future : futureList) {
            try {
                result = future.get();
            } catch (Exception e) {
                log.error("future.get exception:", e);
                continue;
            }
            if (result.isSuccess()) {
                rollbackResponse = result.getData();
                if (null == rollbackResponse || null == rollbackResponse.getContractId()) {
                    log.error("contractCategoryService.rollback return null");
                    continue;
                }
                contractIdList.add(rollbackResponse.getContractId());
            } else {
                log.error("contractCategoryService.rollback fail, code:{}, message:{}", result.getCode(), result.getMessage());
            }
        }

        List<ContractCategoryDTO> deliveryContractList = new ArrayList<>();
        for (ContractCategoryDTO contractCategoryDTO : contractCategoryDTOList) {
            for (Long contractId : contractIdList) {
                if (contractId.equals(contractCategoryDTO.getId())) {
                    deliveryContractList.add(contractCategoryDTO);
                    break;
                }
            }
        }
        log.info("RollBackContract deliveryContractList: {}", deliveryContractList);
        if (deliveryContractList.size() != contractCategoryDTOList.size()) {
            log.error("RollBackContractList:{}, deliveryContractList:{}", contractCategoryDTOList, deliveryContractList);
        }

        //交割
        DeliveryRunnable.delivery(deliveryContractList, rollbackTime, DeliveryEffectTypeEnum.ROLL_BACK);

        //解冻所有用户提笔操作
        try {
            OperationAllLimitDTO dto = operationLimitService.getAllLimitInfo(LimitTypeEnum.WITHDRAW_FREEZE.getCode());
            if (dto == null || dto.getId() == null) {
                log.error("operationLimitService.getAllLimitInfo Exception, id is null");
            }
            if (dto.getId() < 0) {
                return ResultCode.error(3,"解冻失败");
            }
            operationAllLimitDTO.setId(dto.getId());
            operationLimitService.deleteOperationAllLimit(operationAllLimitDTO);
        } catch (Exception e) {
            log.error("operationLimitService.deleteOperationAllLimit Exception", e);
            return ResultCode.error(3,"解冻失败");
        }

        return ResultCode.success();

    }
}
