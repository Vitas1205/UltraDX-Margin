package com.fota.fotamargin.common.constant;

/**
 * @author taoyuanming
 * Created on 2018/8/2
 * Description redis
 */
public class RedisConstant {

    /**
     * 正在校验某用户是否爆仓key  margin:forced:liquidation:user:{uid} 简化为  margin:for:liq:u:{uid}
     */
    public static final String RUNNING_FORECD_LIQUIDATION_USER_ = "margin:for:liq:u:";

    /**
     * 警告保证金不足key
     */
    public static final String MARGIN_WARNING = "margin:mar:warn:u:";

    /**
     * 警告保证金不足时间间隔 12小时
     */
    public static final Long MARGIN_WARNING_EXPIRE_TIME = 12 * 60 * 60L;

    /**
     * 正在校验某用户是否爆仓key  margin:forced:liquidation:user:{uid} 简化为  margin:for:liq:u:{uid}
     */

    /**
     * 爆仓通知用户记录
     */
    public static final String FORECD_LIQUIDATION_WARN_USER_RECORD = "margin:for:liq:w:u:record";

    /**
     * 标志交割完成的key
     */
    public static final String MARGIN_DELIVERY_JOB_DONE = "margin:delivery:job:done";

    /**
     * 强平单信息
     */
    public static final String FORCE_ORDER_INFO = "margin:force:orderInfo";

}
