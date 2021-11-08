package com.fota.fotamargin.common.util.email;

/**
 * @author jintian on 2018/6/24.
 * @see
 */
public enum MailOperatorEnum {

    REGISTER_MAIL(0, "register"),
    CODE_MAIL(1,"send coupon code"),
    RECHANGE_PWD(2,"register"),
    ;

    private Integer code;

    private String text;

    MailOperatorEnum(Integer type, String text) {
        this.code = type;
        this.text = text;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getText() {
        return this.text;
    }

    public static MailOperatorEnum getMailOperatorEnumByCode(Integer targetCode) {
        for (MailOperatorEnum mailOperatorEnum : MailOperatorEnum.values()) {
            if (mailOperatorEnum.getCode().equals(targetCode)) {
                return mailOperatorEnum;
            }
        }
        return null;
    }
//
//    public static String getMsg(MailOperatorEnum operatorEnum, Integer language) {
//        switch (language){
//            case 1:
//                return getChineseMsg(operatorEnum);
//            case 2:
//                return getEnglishMsg(operatorEnum);
//            default:
//                return "没有模版";
//        }
//    }
//
//    private static String getChineseMsg(MailOperatorEnum operatorEnum) {
//        switch (operatorEnum) {
//            case REGISTER_MAIL:
//                return Template.EMAIL_CH_REGISTER;
//            case CODE_MAIL:
//                return Template.EMAIL_CH_CODE;
//            default:
//                return "没有模版";
//        }
//    }
//
//    private static String getEnglishMsg(MailOperatorEnum operatorEnum) {
//        switch (operatorEnum) {
//            case REGISTER_MAIL:
//                return Template.EMAIL_EN_REGISTER;
//            case CODE_MAIL:
//                return Template.EMAIL_EN_CODE;
//            default:
//                return "没有模版";
//        }
//    }

    public static String getTitle(MailOperatorEnum operatorEnum, Integer language) {
        switch (language){
            case 1:
                return getChineseTitle(operatorEnum);
            case 2:
                return getEnglishTitle(operatorEnum);
            default:
                return "没有模版";
        }
    }

    private static String getChineseTitle(MailOperatorEnum operatorEnum) {
        switch (operatorEnum) {
            case REGISTER_MAIL:
                return "【FOTA】注册成功";
            case CODE_MAIL:
                return "【FOTA】安全验证";
            default:
                return "没有模版";
        }
    }

    private static String getEnglishTitle(MailOperatorEnum operatorEnum) {
        switch (operatorEnum) {
            case REGISTER_MAIL:
                return "【FOTA】registration success";
            case CODE_MAIL:
                return "【FOTA】Security verification";
            default:
                return "没有模版";
        }
    }
}