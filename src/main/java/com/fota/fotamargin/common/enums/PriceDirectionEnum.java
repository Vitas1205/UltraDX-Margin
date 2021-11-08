package com.fota.fotamargin.common.enums;

/**
 * @author Tao Yuanming
 * Created on 2018/9/21
 * Description 买一卖一价
 */
public enum  PriceDirectionEnum {

    /**
     * 买一价
     */
    BUY("bid", 2),

    /**
     * 卖一价
     */
    SELL("ask", 1);

    private String name;
    private int code;

    PriceDirectionEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
