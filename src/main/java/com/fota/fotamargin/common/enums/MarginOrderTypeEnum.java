package com.fota.fotamargin.common.enums;

import lombok.Getter;

/**
 * @author taoyuanming
 * Created on 2018/9/15
 * Description 保证金系统的挂单类型
 */
public enum MarginOrderTypeEnum {
    /**
     * 该类型的挂单的需求保证金 = 挂单保证金 + 挂单手续费
     */
    MARGIN(1, "margin"),

    /**
     * 该类型的挂单的需求保证金 = 挂单手续费
     */
    FEE(2, "fee"),
    ;

    @Getter
    private Integer code;
    private String desc;

    MarginOrderTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
