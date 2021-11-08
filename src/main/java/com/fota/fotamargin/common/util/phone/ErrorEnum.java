package com.fota.fotamargin.common.util.phone;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jintian on 2018/6/22.
 * @see
 */
public enum ErrorEnum {
    /**
     * 默认无错误
     */
    DEFAULT_SUCECESS(0, "默认无错误"),
    NEED_LOGIN(401,"need login"),

    /**
     * 常见错误
     */

    DEFAULT_ERROR(1000, "默认错误"),
    PARAMS_ERROR(1001, "参数错误"),
    HAS_NO_POWER(1002, "没有权限操作"),
    DB_ERROR(1003, "数据库操作错误"),
    DATA_NOT_EXIST(1004, "数据不存在"),
    GOOGLE_BINDING_FAIL(1005,"谷歌验证绑定失败"),
    GOOGLE_UNBINDING_FAIL(1006,"谷歌验证解绑失败"),
    GOOGLE_LOGIN_ACCESS_SWITCH_FAIL(1007,"谷歌登陆操作失败"),
    GOOGLE_CODE_ERROR(1008,"谷歌验证码错误"),
    CAPTCHA_CODE_ERROR(1009,"图形验证码错误"),
    PHONE_CODE_ERROR(1010,"手机验证码错误"),
    EMAIL_CODE_ERROR(1011,"邮箱验证码错误"),
    PICTURE_CODE_ERROR(1012, "获取图片验证码错误"),
    VERIFY_CODE_ERROR(1013, "验证码错误"),

    EMAIL_UNBIND_ERROR(1014, "未绑定邮箱"),
    LOGIN_PWD_REPEAT_ERROR(1015, "两次输入登录密码不一致"),


//    GOOGLE_BINDING_FAIL(1014,"谷歌验证绑定失败"),
//    GOOGLE_UNBINDING_FAIL(1015,"谷歌验证解绑失败"),
//    GOOGLE_LOGIN_ACCESS_SWITCH_FAIL(1016,"谷歌登陆操作失败"),
//    GOOGLE_CODE_ERROR(1017,"谷歌验证码错误"),
//    CAPTCHA_CODE_ERROR(1018,"图形验证码错误"),
//    PHONE_CODE_ERROR(1019,"手机验证码错误"),
//    EMAIL_CODE_ERROR(1020,"邮箱验证码错误"),
//    PICTURE_CODE_ERROR(1021, "获取图片验证码错误"),
//    VERIFY_CODE_ERROR(1022, "验证码错误"),
    UPDATE_LOGIN_PWD_FAIL(1023,"更新登陆密码失败"),
    SEND_VERIFICATION_CODE(1024,"发送验证码失败"),
    NOT_FOUND_USER(1025,"该用户不存在"),
    IS_GOOGLE_LOGIN(1026,"开启登录谷歌验证"),
    EMAIL_NO_TYPE(1027,"没有对应的邮件类型"),
    NOT_NULL(1028,"不能为空"),
    PASSWORD_ERROR(1029,"用户名或密码错误"),
    NOT_THE_SAME(1030,"两次密码不一样"),
    ALLREADY_EXIST(1031,"已经存在"),
//    FUND_PASSWORD_ERROR(1025, "资金密码错误"),
    PASSWORD_REPEAT_ERROR(1033, "新老密码重复"),
    FILE_UPLOAD_ERROR(1034, "文件上传错误"),
    UNSUPPORT_AUTHENTICATE_TYPE(1035, "不支持的身份认证类型"),
    VERIFY_TYPE_NOT_AUTH(1036, "验证类型未认证"),
    PHONE_UNBIND_ERROR(1037,"未绑定手机"),
    FUND_PASSWORD_ERROR(1038, "资金密码错误"),
    UNKNOWN_ERROR(-1, "未知错误"),
    SEND_SMS_CODE_FAIL(1039, "发送短信验证码失败"),
    SEND_EMAIL_CODE_FAIL(1040, "发送邮箱验证码失败"),
    PHONE_NO_TYPE(1041,"没有对应的短信类型"),
    VERIFICATION_FAIL(1042,"验证失败"),
    VERIFY_OLD_PWD_FAIL(1043,"原密码错误"),
    TOO_MANY_FAILS(1044,"失败次数过多"),

    GET_CURRENT_LOGIN_USER_ERROR(1045, "获取当前登录用户失败"),
    ACCOUNT_NOT_EXIST_ERROR(1047, "用户不存在"),
    RPC_CALL_ERROR(1048, "RPC调用失败"),
    GET_USER_SECURITY_INFO_ERROR(1049,"获取用户安全设置信息失败"),
    NOT_SUPPORT_CONTRACT_TYPE(1050, "不支持的合约交易类型"),
    ID_CARD_FORMAT_ERROR(1051, "身份证格式错误"),
    BIND_EMIAL_PRECONDTION_ERROR(1052, "绑定邮箱前置条件未满足"),
    SET_FUND_PASSWORD_PRECONDTION_ERROR(1053, "设置资金密码前置条件未满足"),
    MODIFU_FUND_PASSWORD_PRECONDTION_ERROR(1054, "修改资金密码前置条件未满足"),
    RESET_FUND_PASSWORD_PRECONDTION_ERROR(1055, "忘记资金密码前置条件未满足"),
    EMAIL_HAS_BIND_ERROR(1056, "邮箱已绑定，不能再修改"),
    EMAIL_ADDRESS_ERROR(1057, "邮件地址异常"),
    THIS_EMAIL_ALREADY_BIND(1058, "该邮箱已经被绑定"),

    REMOTE_SERVICE_FAIL(1072, "RPC接口调用失败"),
    USER_LOGIN_FAIL(1073, "用户登录校验失败"),
    GET_USER_SESSION_FAIL(401, "用户登录校验失败"),
    ILLEGAL_PARAM(1075, "参数校验失败"),
    CAPTIMAL_AMOUNT_NOT_ENOUGH(1076, "钱包账户余额不足"),
    CONTRACT_AMOUNT_NOT_ENOUGH(1077, "合约账户余额不足"),
    AMOUNT_ILLEGAL(1078, "金额不合法"),
    ATRANSFER_TYPE_ILLEGAL(1079, "交易类型错误"),
    FUND_PASSWORD_NOT_BIND(1080, "资金密码未绑定"),
    GOOGLE_AUTH_NOT_BIND(1081, "没有开启谷歌认证"),
    PHONE_AND_EMAIL_NOT_BIND(1082, "手机和邮箱都未认证"),
    GET_GOOGLE_SECRET_KEY_FAIL(1083, "获取谷歌二维码失败"),
    DATE_AFTER(1084, "密码错误超过3次，锁定10分钟"),
    SMS_SEND_ERROR(1085, "发送短信失败"),
    VERIFY_CODE_TIMEOUT(1086, "验证码已失效"),
    PHONE_HAS_BINDED(1088,"该手机已被绑定"),

    NOT_FIND_ANY_CONTRACT_OR_USDK_ERROR(1089,"没有查到任何合约和USDK"),
    ALREADY_ENOUGH_CARD_ERROR(1090, "用户已有足够的卡片"),
    GET_DEFAULT_CARD_ERROR(1091, "获取默认的卡片信息失败"),
    PRICE_OUT_OF_BOUNDARY(1092, "价格超出指定范围"),
    LEVER_ILLEGAL_BY_MC(1093, "杠杆调整不符合保证金限制"),
    PRICE_TYPE_ILLEGAL(1094, "价格类型不合法"),

    ILLEGAL_COIN_ADDRESS(1300,"输入地址校验失败"),
    WITHDRAW_FAILED(1301,"提币失败"),
    GOOGLE_UNBINDING_CLOSE_GOOLE_LOGIN_FIRST(1302,"请先关闭谷歌登陆"),
    COLLECTION_NULL(1303,"还没有收藏"),
    
    VERITY_TRADE_PASSWORD_TIMEOUT(1304,"交易密码失效"),
    UNFILLED_ORDER_EXIST(1401,"调整杠杆失败，有未完成的订单"),
    SYSTEM_ERROR(1402, "系统异常"),
    LOGIN_LOCKED_ERROR(1403, "密码输入失败次数过多，登录被冻结"),
    FUND_PASSWORD_LOCKED(1404, "资金密码输入失败次数过多，相关操作被冻结"),
    PHONE_REGISTER_LOCKED(1405, "手机注册失败次数过多，该ip手机注册被冻结"),
    EMAIL_REGISTER_LOCKED(1406, "邮箱注册失败次数过多，该ip邮箱注册被冻结"),
    SECURITY_OPERATE_LOCKED(1407, "进行安全操作后提币被冻结24h"),
    RESET_LOGIN_PWD_LOCKED_PHONE(1408, "短信验证码错误次数过多，请稍后重试"),
    RESET_LOGIN_PWD_LOCKED_EMAIL(1409, "邮箱验证码错误次数过多，请稍后重试"),
    RESET_LOGIN_PWD_LOCKED_GOOGLE(1410, "谷歌验证码错误次数过多，请稍后重试"),

    THIS_OPERATION_HAS_LIMITED(1500, "此操作已经被冻结了"),
    THIS_OPERATION_HAS_ALL_LIMITED(1501, "此操作已被全网冻结了"),
    THIS_IP_HAS_LIMITED(1502, "该ip已经被限制访问"),
    THIS_USER_LOGIN_HAS_BEEN_FROZEN(1503,"该用户登录已被冻结"),
    THIS_USER_REGISTER_HAS_BEEN_FROZEN(1504,"该邮箱或手机已被限制注册"),
    ;

    private static Map<Integer, ErrorEnum> map = new HashMap<>();

    static {
        Class<ErrorEnum> clazz = ErrorEnum.class;
        for(ErrorEnum obj: clazz.getEnumConstants()){
            map.put(obj.code, obj);
        }
    }


    /**
     * 值
     */
    private int code;

    /**
     * 名称
     */
    private String desc;


    public int getCode() {
        return code;
    }

    public void setCode(int value) {
        this.code = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    ErrorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ErrorEnum getErrorEnumByCode(Integer code) {
        return map.get(code);
    }

    @Override
    public String toString() {
        return "ErrorEnum{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
