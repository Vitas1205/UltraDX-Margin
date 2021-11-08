package com.fota.fotamargin.common.util;

import java.util.UUID;

/**
 * @author taoyuanming
 * Created on 2018/7/23
 * Description
 */
public class FUUID {


    /**
     * 获取唯一标示
     * @return
     */
    public static String getUUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
