package com.fota.fotamargin.manager.trade;

import com.fota.fotamargin.dao.trade.mapper.ContractCategoryMapper;
import com.fota.trade.client.domain.ContractCategoryDO;
import com.fota.trade.domain.ContractCategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuanming Tao
 * Created on 2018/11/16
 * Description
 */
@Service
public class ContractCategoryManager {
    private static final Logger log = LoggerFactory.getLogger(ContractCategoryManager.class);

    @Autowired
    private ContractCategoryMapper contractCategoryMapper;
    /**
     * 根据合约状态获取合约详情
     *
     * @param status
     * @return
     */
    public List<ContractCategoryDTO> getContractByStatus(Integer status) {
        if (status <= 0) {
            return null;
        }
        try {
            List<ContractCategoryDO> listDO = contractCategoryMapper.selectByStatus(status);
            List<ContractCategoryDTO> listDTO = new ArrayList<ContractCategoryDTO>();
            if (!CollectionUtils.isEmpty(listDO)){
                for(ContractCategoryDO temp : listDO){
                    listDTO.add(copy(temp));
                }
            }
            return listDTO;
        } catch (Exception e) {
            log.error("contractCategoryMapper.selectByStatus({})", status, e);
        }
        return new ArrayList<ContractCategoryDTO>();
    }

    public static ContractCategoryDTO copy(ContractCategoryDO contractCategoryDO) {
        ContractCategoryDTO contractCategoryDTO = new ContractCategoryDTO();
        contractCategoryDTO.setId(contractCategoryDO.getId());
        contractCategoryDTO.setGmtCreate(contractCategoryDO.getGmtCreate());
        contractCategoryDTO.setGmtModified(contractCategoryDO.getGmtModified());
        contractCategoryDTO.setContractName(contractCategoryDO.getContractName());
        contractCategoryDTO.setAssetId(contractCategoryDO.getAssetId());
        contractCategoryDTO.setAssetName(contractCategoryDO.getAssetName());
//        contractCategoryDTO.setTotalAmount(contractCategoryDO.getTotalAmount());
//        contractCategoryDTO.setUnfilledAmount(contractCategoryDO.getUnfilledAmount());
        contractCategoryDTO.setDeliveryDate(contractCategoryDO.getDeliveryDate().getTime());
        contractCategoryDTO.setStatus(contractCategoryDO.getStatus());
        contractCategoryDTO.setContractType(contractCategoryDO.getContractType());
        return contractCategoryDTO;
    }
}
