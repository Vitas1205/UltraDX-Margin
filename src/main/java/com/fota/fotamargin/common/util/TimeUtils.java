package com.fota.fotamargin.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author taoyuanming
 * Created on 2018/8/7
 * Description
 */
public class TimeUtils {


    private static ThreadLocal<SimpleDateFormat> LOCAL_SDF = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
        }
    };

    /**
     * (获取指定时区日历)
     * @return
     */
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public static long getTimeInMillis() {
        return getCalendar().getTimeInMillis();
    }

    /**
     * 获得当前时间
     * @return
     */
    public static String getCurrentTime() {
        return LOCAL_SDF.get().format(getCalendar().getTime());
    }

    /**
     * 获得当前时间 精确到毫秒
     * @return
     */
    public static String getCurrentMTime() {
        return LOCAL_SDF.get().format(getCalendar().getTime());
    }

    /**
     * 格式化指定毫秒数
     * @param milliseconds
     * @return
     */
    public static String formatMillisecondsToString(long milliseconds) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(milliseconds);
        return LOCAL_SDF.get().format(calendar.getTime());
    }

    /**
     * 获取当前交割时间
     * @return
     */
    public static long getDeliveryMillis() {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前交割时间
     * @return
     */
    public static long getDeliveryDateMillis() {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前交割时间
     * @return
     */
    public static long getDeliveryMillis(long milliseconds) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(milliseconds);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
//
//    /**
//     * 获取下一交割时间
//     * @param currDeliveryMillis
//     * @param contractType
//     * @return
//     */
//    public static long calculateNextDeliveryMillis(long currDeliveryMillis, Integer contractType) {
//        Calendar calendar = getCalendar();
//        calendar.setTimeInMillis(currDeliveryMillis);
//        //校准为16点
//        calendar.set(Calendar.HOUR_OF_DAY, 16);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//
//        if (ContractTypeEnum.WEEK.getCode().equals(contractType)) {
//            //每周五的下午4点
//            calendar.add(Calendar.DATE, 7);
//        } else if (ContractTypeEnum.MONTH.getCode().equals(contractType)) {
//            //每月最后一个周四下午4点整
//            calendar.add(Calendar.MONTH, 1);
//            int month = calendar.get(Calendar.MONTH);
//            calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 5);
//            calendar.set(Calendar.DAY_OF_WEEK, 5);
//            if (month != calendar.get(Calendar.MONTH)) {
//                calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
//            }
//        } else {
//            //每月最后一个周三下午4点整
//            calendar.add(Calendar.MONTH, 3);
//            int month = calendar.get(Calendar.MONTH);
//            calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 5);
//            calendar.set(Calendar.DAY_OF_WEEK, 4);
//            if (month != calendar.get(Calendar.MONTH)) {
//                calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
//            }
//        }
//        return calendar.getTimeInMillis();
//    }

    /**
     * 获取下一交割时间
     * @param currDeliveryMillis
     * @param contractType
     * @return
     */
    public static long calculateNextDeliveryMillis(long currDeliveryMillis, Integer contractType) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(currDeliveryMillis);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, 3);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取交割日期
     * @return
     */
    public static String getDeliveryDateStr(long milliseconds) {
        return formatMillisecondsToString(milliseconds).substring(2, 6);
    }

    /**
     * 得到获取交割指数所需的交割时间
     * @param milliseconds
     * @return
     */
    public static long getDeliveryMillisForIndex(long milliseconds) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(milliseconds);
        calendar.set(Calendar.MILLISECOND, 500);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定毫秒数
     * @param milliseconds
     * @return
     */
    public static long getMiddleMillis(long milliseconds) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(milliseconds);
        calendar.set(Calendar.MILLISECOND, 500);
        return calendar.getTimeInMillis();
    }

    public static long get23Hour() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -23);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }
}
