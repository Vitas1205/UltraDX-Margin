package com.fota.fotamargin.common.enums;

/**
 * @author taoyuanming
 * Created on 2018/8/7
 * Description 标的物
 */
public enum CoinSymbolEnum {


    /**
     * BTC
     */
    BTC(0, "BTC"),

    /**
     * ETH
     */
    ETH(1, "ETH"),

    /**
     * EOS
     */
    EOS(2, "EOS"),

    /**
     * BCH
     */
    BCH(3, "BCH"),

    /**
     * ETC
     */
    ETC(4, "ETC"),

    /**
     * LTC
     */
    LTC(5, "LTC"),

    /**
     * 当前交易合约的标的物：BTC ETH
     */
    CONTRACT_SYMBOL(-1, BTC.getName() + "," + ETH.getName()),;


    private Integer symbol;

    private String name;

    CoinSymbolEnum(Integer symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public static Integer getSymbol(String name) {
        for (CoinSymbolEnum coinSymbolEnum : CoinSymbolEnum.values()) {
            if (coinSymbolEnum.getName().equals(name)) {
                return coinSymbolEnum.getSymbol();
            }
        }
        return null;
    }

    public Integer getSymbol() {
        return symbol;
    }

    public void setSymbol(Integer symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
