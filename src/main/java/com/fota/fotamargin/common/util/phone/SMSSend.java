package com.fota.fotamargin.common.util.phone;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * @author jiguang.qi
 * Created on 2018/6/21
 */

@Slf4j
public class SMSSend {

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    //腾讯短信
    //appid
    private static final int APPID = 1400133843;
    //appkey
    private static final String APPKEY = "9b61bf6e44800de94a8fc91bcfdd4aec";

    private static final int TEMP_ID = 113304;

    private static SmsSingleSender sender;

    static {
        try {
            sender = new SmsSingleSender(APPID,APPKEY);
        } catch (Exception e) {
            log.error("appid or appkey is invalid!", e);
        }
    }


    /**
     * 短信验证码
     * @param phoneCountryCode 手机号国家码
     * @param phone 手机号
     * @param validCode 验证码
     * @param validTime 有效时间/min
     * @return 返回为0表示发送成功
     */
    public static Result sendSMSCode(String phoneCountryCode, String phone, Integer validCode, Integer validTime, Integer templateId) {
        log.info("sendSMSCode CountryCode:{}, phone:{}, code:{}, templateId:{},validTime:{}",
                phoneCountryCode, phone, validCode, templateId, validTime);
        if (validTime == null || validTime == 0) {
            return Result.fail(ErrorEnum.PARAMS_ERROR, "验证码有效时间必须大于0");
        }

        ArrayList<String> params = new ArrayList<>();
        params.add(validCode + "");
        return sendSMSWithParam(phoneCountryCode, phone, templateId, params);
    }

    /**
     * 短信验证码
     *
     * @param phoneCountryCode 手机号国家码
     * @param phone            手机号
     * @param templateId       模板id
     * @return 返回为0表示发送成功
     */
    public static SmsSingleSenderResult sendSMS(String phoneCountryCode, String phone, Integer templateId) throws Exception {
        ArrayList<String> params = new ArrayList<String>();
        return sendSM(phoneCountryCode, phone, templateId, params);
    }

    /**
     * 发送短信
     *
     * @param phoneCountryCode 手机号国家码
     * @param phone            手机号
     * @param templateId       模板id
     * @param params           参数
     * @return 返回为0表示发送成功
     */
    public static SmsSingleSenderResult sendSM(String phoneCountryCode, String phone, Integer templateId, ArrayList<String> params) throws Exception {
        return sender.sendWithParam(phoneCountryCode, phone, templateId, params, "", "", "");
    }

    /**
     * 发送短信
     * @param phoneCountryCode 手机号国家码
     * @param phone 手机号
     * @param templateId 模板id
     * @param params 参数
     * @return 返回为0表示发送成功
     */
    public static Result sendSMSWithParam(String phoneCountryCode, String phone, Integer templateId, ArrayList<String> params) {
        log.info("sendSMSWithParam CountryCode:{}, phone:{}, templateId:{},params:{}",
                phoneCountryCode, phone, templateId, params);

        SmsSingleSenderResult result = null;
        String errorMsg;
        try {
            result = sender.sendWithParam(phoneCountryCode, phone, templateId, params, "", "", "");
            if(result.result == 0) {
                return Result.success();
            }else {
                errorMsg = result.errMsg;
                log.error("sendSMSWithParam failed, CountryCode:{}, phone:{}, params:{}, templateId:{},errMsg:{}",
                        phoneCountryCode, phone, params, templateId, errorMsg);
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
            log.error("sendSMSWithParam except, CountryCode:{}, phone:{}, code:{}, templateId:{}, e:",
                    phoneCountryCode, phone, params, templateId, e);
        }
        return Result.fail(ErrorEnum.SMS_SEND_ERROR, errorMsg);
    }
}
