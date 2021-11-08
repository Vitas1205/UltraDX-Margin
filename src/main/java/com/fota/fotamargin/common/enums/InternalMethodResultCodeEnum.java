package com.fota.fotamargin.common.enums;

/**
 * @author taoyuanming
 * Created on 2018/8/11
 * Description 内部方法返回码
 */
public enum InternalMethodResultCodeEnum {

    /**
     * success
     */
    SUCCESS(0, "success"),

    /**
     * error
     */
    ERROR(1, "error"),;


    private Integer code;

    private String desc;


    InternalMethodResultCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
