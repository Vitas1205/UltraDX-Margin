package com.fota.fotamargin.common.enums;

import lombok.Getter;

/**
 * @author taoyuanming
 * Created on 2018/8/4
 * Description
 */
public enum PositionTypeEnum {
    /**
     * 多仓
     */
    OVER(2, "long"),
    /**
     * 空仓
     */
    EMPTY(1, "short"),
    ;

    @Getter
    private Integer code;
    private String desc;

    PositionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
