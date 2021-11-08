package com.fota.fotamargin.common.enums;

import lombok.Getter;

/**
 * @author taoyuanming
 * Created on 2018/8/4
 * Description 成交方向
 */
public enum DeliveryDirectionEnum {
    /**
     * 多
     */
    LONG(1, "long"),
    /**
     * 空
     */
    SHORT(2, "short"),
    ;

    @Getter
    private Integer code;
    private String desc;

    DeliveryDirectionEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
