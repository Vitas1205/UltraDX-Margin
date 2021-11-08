package com.fota.fotamargin.manager;

import com.fota.fotamargin.dao.domain.MarginDealRecordDO;
import com.fota.fotamargin.dao.mapper.MarginDealRecordDOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author taoyuanming
 * Created on 2018/8/8
 * Description 交割
 */
@Service
public class DeliveryManager {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MarginDealRecordDOMapper marginDealRecordDOMapper;

    /**
     * 新增交割记录
     * @param marginDealRecordDO
     * @return
     */
    public int save(MarginDealRecordDO marginDealRecordDO) {
        return marginDealRecordDOMapper.insertSelective(marginDealRecordDO);
    }
}
