package com.fota.fotamargin.common.entity;

import java.math.BigDecimal;

/**
 * @author taoyuanming
 * Created on 2018/8/6
 * Description 爆仓流程循环操作返回结果
 */
public class LoopOperationResult {

    private BigDecimal t1;

    private Integer code;

    public LoopOperationResult(BigDecimal t1, Integer code) {
        this.t1 = t1;
        this.code = code;
    }

    public BigDecimal getT1() {
        return t1;
    }

    public void setT1(BigDecimal t1) {
        this.t1 = t1;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
