package com.fota.fotamargin.exception;

/**
 * @author taoyuanming
 * Created on 2018/8/14
 * Description
 */
public class MarginException extends Exception {

    public MarginException(String message) {
        super(message);
    }

    public MarginException(String message, Throwable cause) {
        super(message, cause);
    }
}
