package com.fota.fotamargin.common.enums;

import lombok.Getter;

/**
 * @author taoyuanming
 * Created on 2018/8/6
 * Description 爆仓流程循环操作返回结果码
 */
public enum LoopOperationResultCodeEnum {

    /**
     * 返回
     */
    RETURN(1, "方法返回"),

    /**
     * 继续执行
     */
    CONTINUE(2, "方法继续执行"),
    ;

    @Getter
    private Integer code;
    private String desc;

    LoopOperationResultCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
