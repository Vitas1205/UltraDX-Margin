package com.fota.fotamargin.common.enums;

import lombok.Getter;

/**
 * @author taoyuanming
 * Created on 2018/9/23
 * Description 交割上新类型
 */
public enum DeliveryEffectTypeEnum {
    /**
     * 定时任务自动交割
     */
    AUTO(1, "定时任务自动交割上新"),

    /**
     * 手动交割不上新新合约
     */
    ARTIFICIALITY_SIMILAR_AUTO(2,"类似定时任务自动交割上新-手动交割并且上新新合约"),

    /**
     * 手动交割不上新新合约
     */
    ROLL_BACK(3,"回滚触发的交割上新"),
    ;

    @Getter
    private Integer code;
    private String desc;

    DeliveryEffectTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
