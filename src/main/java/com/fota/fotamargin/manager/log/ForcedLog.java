package com.fota.fotamargin.manager.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tao Yuanming
 * Created on 2018/9/6
 * Description
 */
public class ForcedLog {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForcedLog.class);

    public static void forcedLog(String message, Object... params) {
        int i = params.length;
        switch (i) {
            case 1:
                LOGGER.info(message, params[0]);
                break;
            case 2:
                LOGGER.info(message, params[0], params[1]);
                break;
            case 3:
                LOGGER.info(message, params[0], params[1], params[2]);
                break;
            case 4:
                LOGGER.info(message, params[0], params[1], params[2], params[3]);
                break;
            case 5:
                LOGGER.info(message, params[0], params[1], params[2], params[3], params[4]);
                break;
            default:
                LOGGER.info(message);
        }
    }
}
