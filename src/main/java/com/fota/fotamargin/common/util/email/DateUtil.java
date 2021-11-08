package com.fota.fotamargin.common.util.email;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author huangtao 2018/7/12 下午4:04
 * @Description 时间工具
 */
public class DateUtil {

    public static final String webUrl1 = "http://www.baidu.com";

    private static ThreadLocal<SimpleDateFormat> LOCAL_SDF = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };


    /**
     * 指定日期转换格式
     * @param date 被转换的日期
     * @param str 需要转换的格式
     * @return 日期字符串
     */
    public static String formatToString(Date date, String str){
        LOCAL_SDF.get().applyPattern(str);
        return LOCAL_SDF.get().format(date);
    }
}
