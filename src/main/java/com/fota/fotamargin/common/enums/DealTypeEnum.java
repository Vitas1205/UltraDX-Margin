package com.fota.fotamargin.common.enums;

import lombok.Getter;

/**
 * @author taoyuanming
 * Created on 2018/8/4
 * Description 成交类型
 */
public enum DealTypeEnum {
    /**
     * 交割
     */
    DELIVERY(1, "交割"),
    ;

    @Getter
    private Integer code;
    private String desc;

    DealTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
