package com.fota.fotamargin.delivery;

import com.alibaba.fastjson.JSON;
import com.fota.asset.domain.ContractAccountAddAmountDTO;
import com.fota.asset.domain.enums.AssetOperationTypeEnum;
import com.fota.asset.service.AssetWriteService;
import com.fota.common.Result;
import com.fota.fotamargin.common.constant.MarginConstant;
import com.fota.fotamargin.common.enums.DealTypeEnum;
import com.fota.fotamargin.common.enums.DeliveryDirectionEnum;
import com.fota.fotamargin.common.enums.PositionTypeEnum;
import com.fota.fotamargin.common.util.BeanUtil;
import com.fota.fotamargin.common.util.TimeUtils;
import com.fota.fotamargin.dao.domain.MarginDealRecordDO;
import com.fota.fotamargin.manager.DeliveryManager;
import com.fota.fotamargin.manager.MarketAccountListManager;
import com.fota.trade.domain.ContractCategoryDTO;
import com.fota.trade.domain.DeliveryCompletedDTO;
import com.fota.trade.domain.ResultCode;
import com.fota.trade.domain.UserPositionDTO;
import com.fota.trade.service.UserPositionService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author Yuanming Tao
 * Created on 2018/11/14
 * Description
 */
public class PositionRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PositionRunnable.class);

    private static UserPositionService userPositionService = BeanUtil.getBean(UserPositionService.class);
    private static DeliveryManager deliveryManager = BeanUtil.getBean(DeliveryManager.class);
    private static AssetWriteService assetWriteService = BeanUtil.getBean(AssetWriteService.class);
    private static MarketAccountListManager marketAccountListManager = BeanUtil.getBean(MarketAccountListManager.class);

    private Set<Long> userIdList;
    private CountDownLatch countDownLatch;
    private List<UserPositionDTO> userPositionDTOList;
    private BigDecimal index;
    private ContractCategoryDTO deliveryContract;

    public PositionRunnable(Set<Long> userIdList, CountDownLatch countDownLatch, List<UserPositionDTO> userPositionDTOList, BigDecimal index, ContractCategoryDTO deliveryContract) {
        this.userIdList = userIdList;
        this.countDownLatch = countDownLatch;
        this.userPositionDTOList = userPositionDTOList;
        this.index = index;
        this.deliveryContract = deliveryContract;
    }

    @Override
    public void run() {
        try {
            deliveryPosition();
        } catch (Exception e) {
            LOGGER.error("Delivery>>PositionRunnable deliveryPosition exception,", e);
        } finally {
            countDownLatch.countDown();
        }
    }

    private void deliveryPosition() {
        if (CollectionUtils.isEmpty(userPositionDTOList) || index == null || deliveryContract == null) {
            LOGGER.error("Delivery>>PositionRunnable deliveryPosition param error, userPositionDTOList:{}, index:{}, deliveryContract:{}", JSON.toJSONString(userPositionDTOList), index, JSON.toJSONString(deliveryContract));
            return;
        }
        //手续费
        BigDecimal charge;
        //盈亏
        BigDecimal profit;
        DeliveryCompletedDTO deliveryCompletedDTO;
        ResultCode resultCode;
        LOGGER.info("Delivery>>threadPosition:{}, contract:{}, thread:{}", userPositionDTOList.size(), deliveryContract.getContractName(), Thread.currentThread().getName());

        Result<Boolean> result;
        for (UserPositionDTO userPositionDTO : userPositionDTOList) {
            userIdList.add(userPositionDTO.getUserId());
            //用户单个多仓盈亏 = (交割指数-持仓均价）* 持仓数量 * 合约大小  - 手续费
            //用户单个空仓盈亏 = (持仓均价-交割指数）* 持仓数量 * 合约大小  - 手续费
            //手续费=持仓数量*X*（合约大小/成交价对应数字货币数量）*该客户对应手续费率
//            charge = userPositionDTO.getAmount().abs().multiply(index).multiply(userPositionDTO.getContractSize()).multiply(MarginConstant.FEE);
            if (marketAccountListManager.contains(userPositionDTO.getUserId())) {
                charge = BigDecimal.ZERO;
                LOGGER.info("Delivery>>marketAccount userId:{}", userPositionDTO.getUserId());
            } else {
                charge = userPositionDTO.getAmount().abs().multiply(index).multiply(MarginConstant.FEE);
            }
            if (PositionTypeEnum.OVER.getCode().equals(userPositionDTO.getPositionType())) {
//                profit = index.subtract(new BigDecimal(userPositionDTO.getAveragePrice())).multiply(userPositionDTO.getAmount().abs()).multiply(userPositionDTO.getContractSize()).subtract(charge);
                profit = index.subtract(new BigDecimal(userPositionDTO.getAveragePrice())).multiply(userPositionDTO.getAmount().abs()).subtract(charge);
            } else {
//                profit = new BigDecimal(userPositionDTO.getAveragePrice()).subtract(index).multiply(userPositionDTO.getAmount().abs()).multiply(userPositionDTO.getContractSize()).subtract(charge);
                profit = new BigDecimal(userPositionDTO.getAveragePrice()).subtract(index).multiply(userPositionDTO.getAmount().abs()).subtract(charge);
            }
            LOGGER.info("Delivery>>userPositionDTO:{}, delivery-index:{}, charge:{}, profit:{}", JSON.toJSONString(userPositionDTO), index, charge, profit);
            //更新该持仓对应的用户合约账户的盈亏
            ContractAccountAddAmountDTO contractAccountAddAmountDTO = new ContractAccountAddAmountDTO();
            contractAccountAddAmountDTO.setUserId(userPositionDTO.getUserId());
            contractAccountAddAmountDTO.setAddAmount(profit);
            try {
                result = assetWriteService.addContractAmount(contractAccountAddAmountDTO, userPositionDTO.getUserId() + deliveryContract.getId() + System.currentTimeMillis() + "", AssetOperationTypeEnum.CONTRACT_DELIVERY.getCode());
            } catch (Exception e) {
                LOGGER.error("Delivery>>assetWriteService.addContractAmount Exception, contractAccountAddAmountDTO:{}, e:", contractAccountAddAmountDTO, e);
                continue;
            }
            if (result == null || !result.isSuccess() || !result.getData()) {
                LOGGER.error("Delivery>>assetWriteService.addContractAmount fail, contractAccountAddAmountDTO:{}, result:{}", contractAccountAddAmountDTO, result);
            }

            //更新该笔持仓的状态为已交割，并生成交割记录
            deliveryCompletedDTO = new DeliveryCompletedDTO();
            deliveryCompletedDTO.setUserPositionId(userPositionDTO.getId());
            deliveryCompletedDTO.setUserId(userPositionDTO.getUserId());
            deliveryCompletedDTO.setContractId(userPositionDTO.getContractId());
            deliveryCompletedDTO.setContractName(userPositionDTO.getContractName());
            deliveryCompletedDTO.setOrderDirection(userPositionDTO.getPositionType());
            deliveryCompletedDTO.setAmount(userPositionDTO.getAmount());
            deliveryCompletedDTO.setFee(charge);
            deliveryCompletedDTO.setPrice(index);
            try {
                resultCode = userPositionService.deliveryPosition(deliveryCompletedDTO);
            } catch (Exception e) {
                LOGGER.error("Delivery>>userPositionService.deliveryPosition Exception, deliveryCompletedDTO:{}, e:", deliveryCompletedDTO, e);
                continue;
            }
            if (!resultCode.isSuccess()) {
                LOGGER.error("Delivery>>userPositionService.deliveryPosition error, code:{}, message:{}", resultCode.getCode(), resultCode.getMessage());
            }

            //生成该持仓对应的用户的交割记录
            MarginDealRecordDO marginDealRecordDO = new MarginDealRecordDO();
            marginDealRecordDO.setUserId(userPositionDTO.getUserId());
            marginDealRecordDO.setContractName(deliveryContract.getContractName());
            marginDealRecordDO.setDirection(PositionTypeEnum.OVER.getCode().equals(userPositionDTO.getPositionType()) ? DeliveryDirectionEnum.SHORT.getCode() : DeliveryDirectionEnum.LONG.getCode());
            marginDealRecordDO.setAmount(userPositionDTO.getAmount());
            marginDealRecordDO.setPrice(index);
            marginDealRecordDO.setCharge(charge);
            marginDealRecordDO.setType(DealTypeEnum.DELIVERY.getCode());
            marginDealRecordDO.setTime(TimeUtils.getCalendar().getTime());
            try {
                deliveryManager.save(marginDealRecordDO);
            } catch (Exception e) {
                LOGGER.error("Delivery>>deliveryManager.save Exception, marginDealRecordDO:{}, Exception:", marginDealRecordDO, e);
            }
        }
    }
}
