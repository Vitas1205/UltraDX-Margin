package com.fota.fotamargin.manager;

import com.fota.fotamargin.common.constant.Constant;
import com.fota.fotamargin.dao.trade.mapper.UserPositionMapper;
import com.fota.trade.client.domain.UserPositionDO;
import com.fota.trade.domain.UserPositionDTO;
import com.fota.trade.domain.enums.PositionStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuanming Tao
 * Created on 2018/11/15
 * Description
 */
@Service
public class UserPositionManager {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserPositionMapper userPositionMapper;

    public List<UserPositionDTO> listPositionByContractId(Long contractId) {
        try {
            List<UserPositionDO> DOlist = userPositionMapper.selectByContractId(contractId, PositionStatusEnum.UNDELIVERED.getCode());
            List<UserPositionDTO> DTOlist = new ArrayList<>();
            if (DOlist != null && DOlist.size() > 0) {
                for (UserPositionDO tmp : DOlist) {
                    DTOlist.add(copy(tmp));
                }
            }
            return DTOlist;
        }catch (Exception e){
            log.error("listPositionByContractId failed:{}",contractId);
        }
        return null;
    }

    public static UserPositionDTO copy(UserPositionDO userPositionDO) {
        UserPositionDTO userPositionDTO = new UserPositionDTO();
        userPositionDTO.setId(userPositionDO.getId());
        userPositionDTO.setGmtCreate(userPositionDO.getGmtCreate().getTime());
        userPositionDTO.setGmtModified(userPositionDO.getGmtModified().getTime());
        userPositionDTO.setUserId(userPositionDO.getUserId());
        userPositionDTO.setContractId(userPositionDO.getContractId());
        userPositionDTO.setContractName(userPositionDO.getContractName());
        userPositionDTO.setPositionType(userPositionDO.getPositionType());
        userPositionDTO.setAveragePrice(userPositionDO.getAveragePrice().toPlainString());
        userPositionDTO.setAmount(userPositionDO.getUnfilledAmount());
        userPositionDTO.setContractSize(BigDecimal.ONE);
        if (userPositionDO.getFeeRate() != null){
            userPositionDTO.setFeeRate(userPositionDO.getFeeRate());
        }else {
            userPositionDTO.setFeeRate(Constant.FEE_RATE);
        }
        return userPositionDTO;
    }


    public List<UserPositionDTO> listPositionByUserId(long userId) {
        try {
            List<UserPositionDO> DOlist = userPositionMapper.selectByUserId(userId, PositionStatusEnum.UNDELIVERED.getCode());
            List<UserPositionDTO> DTOlist = new ArrayList<>();
            if (DOlist != null && DOlist.size() > 0) {
                for (UserPositionDO tmp : DOlist) {
                    DTOlist.add(copy(tmp));
                }
            }
            return DTOlist;
        }catch (Exception e){
            log.error("listPositionByUserId Exception:{}",userId);
            log.error("listPositionByUserId Exception:", e);
        }
        return null;
    }

}
