package com.fota.fotamargin.common.enums;

/**
 * @author Gavin Shen
 * @Date 2018/7/5
 */
public enum ContractTypeEnum {

    /**
     * 周
     */
    WEEK(1, "WEEK"),
    /**
     * MONTH
     */
    MONTH(2, "MONTH"),
    /**
     * SEASON
     */
    SEASON(3, "SEASON"),
    ;

    private Integer code;
    private String desc;

    ContractTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static Integer getCode(Integer code) {
        for (ContractTypeEnum contractTypeEnum : ContractTypeEnum.values()) {
            if (contractTypeEnum.getCode().equals(code)) {
                return code;
            }
        }
        return null;
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
