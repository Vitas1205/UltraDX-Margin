package com.fota.fotamargin.common.util;

import java.util.Random;

/**
 * @author jintian on 2018/6/25.
 * @see
 */
public class CodeRandomUtil {
    public static String getCode() {
        Random rad = new Random();
        String result = rad.nextInt(1000000) + "";
        if (result.length() != 6) {
            return getCode();
        }
        return result;
    }
}
