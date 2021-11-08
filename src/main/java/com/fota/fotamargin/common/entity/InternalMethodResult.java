package com.fota.fotamargin.common.entity;

import com.fota.fotamargin.common.enums.InternalMethodResultCodeEnum;

/**
 * @author taoyuanming
 * Created on 2018/8/11
 * Description 系统内部方法返回
 */
public class InternalMethodResult {

    private Integer code;

    private Object res;

    private String msg;

    public static InternalMethodResult success() {
        InternalMethodResult result = new InternalMethodResult();
        result.setCode(InternalMethodResultCodeEnum.SUCCESS.getCode());
        return result;
    }

    public static InternalMethodResult success(Object res) {
        InternalMethodResult result = new InternalMethodResult();
        result.setCode(InternalMethodResultCodeEnum.SUCCESS.getCode());
        result.setRes(res);
        return result;
    }

    public static InternalMethodResult error() {
        InternalMethodResult result = new InternalMethodResult();
        result.setCode(InternalMethodResultCodeEnum.ERROR.getCode());
        return result;
    }


    public static InternalMethodResult error(String msg) {
        InternalMethodResult result = new InternalMethodResult();
        result.setCode(InternalMethodResultCodeEnum.ERROR.getCode());
        result.setMsg(msg);
        return result;
    }



    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getRes() {
        return res;
    }

    public void setRes(Object res) {
        this.res = res;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
