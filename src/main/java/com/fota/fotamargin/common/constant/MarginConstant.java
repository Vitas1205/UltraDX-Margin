package com.fota.fotamargin.common.constant;

import java.math.BigDecimal;

/**
 * @author taoyuanming
 * Created on 2018/8/4
 * Description
 */
public class MarginConstant {

    /**
     * FOTA支持的最大可选杠杆
     */
    public static final int MAX_LEVERAGE = 10;

    /**
     * 需要判断爆仓的实时保证金率
     */
    //public static final BigDecimal THRESHOLD_MC = new BigDecimal(1D / MAX_LEVERAGE).setScale(3, BigDecimal.ROUND_HALF_UP);
    public static final BigDecimal THRESHOLD_MC = new BigDecimal(1D / MAX_LEVERAGE);

    /**
     * 手续费率
     */
    public static final BigDecimal FEE = new BigDecimal("0.0005");

    /**
     * 标示 买
     */
    public static final String LONG = "l";

    /**
     * 标示 卖
     */
    public static final String SHORT = "s";

    public static final BigDecimal LONG_MULTIPLE = new BigDecimal(1 - 0.1 / MarginConstant.MAX_LEVERAGE);

    public static final BigDecimal SHORT_MULTIPLE = new BigDecimal(1 + 0.1 / MarginConstant.MAX_LEVERAGE);

    public static final BigDecimal T1 = new BigDecimal("0.8");

    public static final BigDecimal T2 = new BigDecimal("0.6");

    /**
     * 强平BigDecimal scale
     */
    public static final int SCALE = 16;

    /**
     * 强平通知中心邮件短息金额小数位 BTC合约强平价小数位
     */
    public static final int BTC_FORCE_PRICE_SCALE = 1;

    /**
     * 强平通知中心邮件短息金额小数位 BTC合约仓位平仓价值小数位
     */
    public static final int BTC_FORCE_VALUE_SCALE = 8;

    /**
     * 强平通知中心邮件短息金额小数位 ETH合约强平价小数位
     */
    public static final int ETH_FORCE_PRICE_SCALE = 2;

    /**
     * 强平通知中心邮件短息金额小数位 ETH合约仓位平仓价值小数位
     */
    public static final int ETH_FORCE_VALUE_SCALE = 8;
}

