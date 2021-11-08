package com.fota.fotamargin.service.impl;

import com.fota.fotamargin.manager.RollBackManager;
import com.fota.margin.domain.ResultCode;
import com.fota.margin.domain.RollBackReqDto;
import com.fota.margin.service.MarginRollBackService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author taoyuanming
 * Created on 2018/8/8
 * Description
 */
public class MarginRollBackServiceImpl implements MarginRollBackService {

    @Autowired
    private RollBackManager rollBackManager;

    @Override
    public ResultCode rollBackContract(RollBackReqDto rollBackReqDto) {
        return rollBackManager.rollBackContract(rollBackReqDto);
    }
}
