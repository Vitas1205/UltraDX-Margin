package com.fota.fotamargin.common.util.email;

import com.fota.fotamargin.common.util.BeanUtil;
import com.fota.fotamargin.config.PropertiesConfig;

import java.util.Date;

/**
 * @author jintian on 2018/6/24.
 * @see
 */
public class Template {
    private static PropertiesConfig CONFIG = BeanUtil.getBean(PropertiesConfig.class);

    private static final String PICTURE_1 = CONFIG.getPictureA();
    private static final String PICTURE_2 = CONFIG.getPictureB();
    private static final String PICTURE_3 = CONFIG.getPictureC();
    private static final String PICTURE_4 = CONFIG.getPictureD();
    private static final String URL = CONFIG.getUrl();

    /**
     * 保证金不足邮件详情跳转地址
     */
    private static final String MARGIN_TO_URL = URL + "/comm/fund/futuresaccount";
    /**
     * 强平邮件详情跳转地址
     */
    private static final String FORCE_TO_URL = URL + "/trade/contractTrading";
    /**
     * 穿仓邮件详情跳转地址
     */
    private static final String PENETRATE_TO_URL = URL + "/comm/fund/futuresaccount";

//    private static final String PICTURE_1 = "http://172.16.50.194/G1/M00/00/01/rBAyxFuJLQSAFqNRAAAfGCwBHZo231.png";
//    private static final String PICTURE_2 = "http://172.16.50.194/G1/M00/00/01/rBAyxFuKLIWAIbamAAAS218P3nU499.png";
//    private static final String PICTURE_3 = "http://172.16.50.194/G1/M00/00/01/rBAyxFuKLIuASVIBAAAIa5fzdKM365.png";
//    private static final String PICTURE_4 = "http://172.16.50.194/G1/M00/00/01/rBAywluKLIiALLYqAAAvXPAe_R4572.png";
//
//    private static final String CONTRACT_TRADE_URL = "www.fota.com/black/trade";

    /**
     * 邮箱中文验证码
     */
    public static final String EMAIL_CH_CODE = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
            "    <title>Document</title>\n" +
            "    <style>\n" +
            "        * {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        .mailWrap {\n" +
            "            width: 640px;\n" +
            "            margin: 0 auto;\n" +
            "        }\n" +
            "        .mailBg {\n" +
            "            width: 100%;\n" +
            "            height: 95px;\n" +
            "            background: url('"+ PICTURE_1 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "        }\n" +
            "        .logo {\n" +
            "            float: left;\n" +
            "            padding-top: 25px;\n" +
            "            padding-left: 40px;\n" +
            "        }\n" +
            "        .mailTitle {\n" +
            "            font-size: 20px;\n" +
            "            color: #fff;\n" +
            "            float: left;\n" +
            "            line-height: 95px;\n" +
            "            margin-left: 152px;\n" +
            "        }\n" +
            "        .mailContent {\n" +
            "            padding: 63px 41px 50px 41px;\n" +
            "        }\n" +
            "        .contentTitle {\n" +
            "            font-size: 18px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 28px;\n" +
            "        }\n" +
            "        .contentInfo {\n" +
            "            margin-top: 30px;\n" +
            "            font-size: 24px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 42px;\n" +
            "        }\n" +
            "        .btn {\n" +
            "            display: block;\n" +
            "            width: 122px;\n" +
            "            height: 42px;\n" +
            "            margin: 0 auto;\n" +
            "            margin-top: 135px;\n" +
            "            background-color: #3d79ec;\n" +
            "\t        border-radius: 21px;\n" +
            "            text-align: center;\n" +
            "            line-height: 42px;\n" +
            "            letter-spacing: 0px;\n" +
            "            color: #ffffff;\n" +
            "            cursor: pointer;\n" +
            "        }\n" +
            "        .mailBottom {\n" +
            "            width: 100%;\n" +
            "            height: 195px;\n" +
            "            padding: 25px 41px 20px 41px;\n" +
            "            background: url('"+ PICTURE_2 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "            border-bottom: 1px solid rgba(213,214,225,0.3);\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftTitle {\n" +
            "            margin-top: 5px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7b7f87;\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftWrap {\n" +
            "            margin-top: 30px;\n" +
            "        }\n" +
            "        .mailBottom-leftContent {\n" +
            "            font-size: 16px;\n" +
            "            line-height: 16px;\n" +
            "            color: #7c8088;\n" +
            "            margin-bottom: 16px;\n" +
            "        }\n" +
            "        .mailBottom-leftTe {\n" +
            "            color: #3d79ec;\n" +
            "        }\n" +
            "        .mailBottom-right {\n" +
            "            float: right;\n" +
            "        }\n" +
            "        .mailCopy {\n" +
            "            width: 100%;\n" +
            "            height: 65px;\n" +
            "            text-align: center;\n" +
            "            line-height: 65px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7c8088;\n" +
            "            background: #fbfbfc;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"mailWrap\">\n" +
            "        <div class=\"mailBg\">\n" +
            "            <img src=\""+ PICTURE_3 +"\" class=\"logo\">\n" +
            "            <p class=\"mailTitle\">安全验证</p>\n" +
            "        </div>\n" +
            "        <div class=\"mailContent\">\n" +
            "           <p class=\"contentTitle\">亲爱的用户：</p>\n" +
            "           <p class=\"contentInfo\">您的验证码为：${code}，30分钟内有效，请勿将验证码告诉他人。若非本人操作，建议您立即更改账户密码。</p>\n" +
            "        </div>\n" +
            "        <div class=\"mailBottom\">\n" +
            "            <div class=\"mailBottom-left\">\n" +
            "                <p class=\"mailBottom-leftTitle\">本邮件为系统发送，请勿回复。</p>\n" +
            "                <div class=\"mailBottom-leftWrap\">\n" +
            "                    <div class=\"mailBottom-leftContent\">FOTA团队： <span class=\"mailBottom-leftTe\">${sendTime}</span></div>\n" +
            "                    <div class=\"mailBottom-leftContent\">客服邮箱：<span class=\"mailBottom-leftTe\">support@fota.com</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"mailCopy\">\n" +
            "            &copy;©2017-2018 FOTA.com. All Rights Reserved\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

    /**
     * 邮箱中文合约交易-补充保证金
     */
    public static final String EMAIL_CH_SUPPLEMENT_MARGIN = "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
            "    <title>Document</title>\n" +
            "    <style>\n" +
            "        * {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        .mailWrap {\n" +
            "            width: 640px;\n" +
            "            margin: 0 auto;\n" +
            "        }\n" +
            "        .mailBg {\n" +
            "            width: 100%;\n" +
            "            height: 95px;\n" +
            "            background: url('"+ PICTURE_1 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "        }\n" +
            "        .logo {\n" +
            "            float: left;\n" +
            "            padding-top: 25px;\n" +
            "            padding-left: 40px;\n" +
            "        }\n" +
            "        .mailTitle {\n" +
            "            font-size: 20px;\n" +
            "            color: #fff;\n" +
            "            float: left;\n" +
            "            line-height: 95px;\n" +
            "            margin-left: 152px;\n" +
            "        }\n" +
            "        .mailContent {\n" +
            "            padding: 63px 41px 50px 41px;\n" +
            "        }\n" +
            "        .contentTitle {\n" +
            "            font-size: 18px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 28px;\n" +
            "        }\n" +
            "        .contentInfo {\n" +
            "            margin-top: 30px;\n" +
            "            font-size: 24px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 42px;\n" +
            "        }\n" +
            "        .btn {\n" +
            "            display: block;\n" +
            "            width: 122px;\n" +
            "            height: 42px;\n" +
            "            margin: 0 auto;\n" +
            "            margin-top: 135px;\n" +
            "            background-color: #3d79ec;\n" +
            "\t        border-radius: 21px;\n" +
            "            text-align: center;\n" +
            "            line-height: 42px;\n" +
            "            letter-spacing: 0px;\n" +
            "            color: #ffffff;\n" +
            "            cursor: pointer;\n" +
            "        }\n" +
            "        .mailBottom {\n" +
            "            width: 100%;\n" +
            "            height: 195px;\n" +
            "            padding: 25px 41px 20px 41px;\n" +
            "            background: url('"+ PICTURE_2 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "            border-bottom: 1px solid rgba(213,214,225,0.3);\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftTitle {\n" +
            "            margin-top: 5px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7b7f87;\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftWrap {\n" +
            "            margin-top: 30px;\n" +
            "        }\n" +
            "        .mailBottom-leftContent {\n" +
            "            font-size: 16px;\n" +
            "            line-height: 16px;\n" +
            "            color: #7c8088;\n" +
            "            margin-bottom: 16px;\n" +
            "        }\n" +
            "        .mailBottom-leftTe {\n" +
            "            color: #3d79ec;\n" +
            "        }\n" +
            "        .mailBottom-right {\n" +
            "            float: right;\n" +
            "        }\n" +
            "        .mailCopy {\n" +
            "            width: 100%;\n" +
            "            height: 65px;\n" +
            "            text-align: center;\n" +
            "            line-height: 65px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7c8088;\n" +
            "            background: #fbfbfc;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"mailWrap\">\n" +
            "        <div class=\"mailBg\">\n" +
            "            <img src=\""+ PICTURE_3 +"\" class=\"logo\">\n" +
            "            <p class=\"mailTitle\">账户资金重要通知</p>\n" +
            "        </div>\n" +
            "        <div class=\"mailContent\">\n" +
            "           <p class=\"contentTitle\">亲爱的用户：</p>\n" +
            "           <p class=\"contentInfo\">您的保证金率过低，请及时补充保证金，否则将被系统强平。</p>\n" +
            "           <a class=\"btn\" href='"+MARGIN_TO_URL+"'>查看详情</a>\n" +
            "        </div>\n" +
            "        <div class=\"mailBottom\">\n" +
            "            <div class=\"mailBottom-left\">\n" +
            "                <p class=\"mailBottom-leftTitle\">本邮件为系统发送，请勿回复。</p>\n" +
            "                <div class=\"mailBottom-leftWrap\">\n" +
            "                    <div class=\"mailBottom-leftContent\">FOTA团队： <span class=\"mailBottom-leftTe\">${sendTime}</span></div>\n" +
            "                    <div class=\"mailBottom-leftContent\">客服邮箱：<span class=\"mailBottom-leftTe\">support@fota.com</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"mailCopy\">\n" +
            "            &copy;©2017-2018 FOTA.com. All Rights Reserved\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

    /**
     * 邮箱中文合约交易-强平
     */
    public static final String EMAIL_CH_FORCE_QUARE = "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
            "    <title>Document</title>\n" +
            "    <style>\n" +
            "        * {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        .mailWrap {\n" +
            "            width: 640px;\n" +
            "            margin: 0 auto;\n" +
            "        }\n" +
            "        .mailBg {\n" +
            "            width: 100%;\n" +
            "            height: 95px;\n" +
            "            background: url('"+ PICTURE_1 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "        }\n" +
            "        .logo {\n" +
            "            float: left;\n" +
            "            padding-top: 25px;\n" +
            "            padding-left: 40px;\n" +
            "        }\n" +
            "        .mailTitle {\n" +
            "            font-size: 20px;\n" +
            "            color: #fff;\n" +
            "            float: left;\n" +
            "            line-height: 95px;\n" +
            "            margin-left: 152px;\n" +
            "        }\n" +
            "        .mailContent {\n" +
            "            padding: 63px 41px 50px 41px;\n" +
            "        }\n" +
            "        .contentTitle {\n" +
            "            font-size: 18px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 28px;\n" +
            "        }\n" +
            "        .contentInfo {\n" +
            "            margin-top: 30px;\n" +
            "            font-size: 24px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 42px;\n" +
            "        }\n" +
            "        .btn {\n" +
            "            display: block;\n" +
            "            width: 122px;\n" +
            "            height: 42px;\n" +
            "            margin: 0 auto;\n" +
            "            margin-top: 135px;\n" +
            "            background-color: #3d79ec;\n" +
            "\t        border-radius: 21px;\n" +
            "            text-align: center;\n" +
            "            line-height: 42px;\n" +
            "            letter-spacing: 0px;\n" +
            "            color: #ffffff;\n" +
            "            cursor: pointer;\n" +
            "        }\n" +
            "        .mailBottom {\n" +
            "            width: 100%;\n" +
            "            height: 195px;\n" +
            "            padding: 25px 41px 20px 41px;\n" +
            "            background: url('"+ PICTURE_2 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "            border-bottom: 1px solid rgba(213,214,225,0.3);\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftTitle {\n" +
            "            margin-top: 5px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7b7f87;\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftWrap {\n" +
            "            margin-top: 30px;\n" +
            "        }\n" +
            "        .mailBottom-leftContent {\n" +
            "            font-size: 16px;\n" +
            "            line-height: 16px;\n" +
            "            color: #7c8088;\n" +
            "            margin-bottom: 16px;\n" +
            "        }\n" +
            "        .mailBottom-leftTe {\n" +
            "            color: #3d79ec;\n" +
            "        }\n" +
            "        .mailBottom-right {\n" +
            "            float: right;\n" +
            "        }\n" +
            "        .mailCopy {\n" +
            "            width: 100%;\n" +
            "            height: 65px;\n" +
            "            text-align: center;\n" +
            "            line-height: 65px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7c8088;\n" +
            "            background: #fbfbfc;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"mailWrap\">\n" +
            "        <div class=\"mailBg\">\n" +
            "            <img src=\""+ PICTURE_3 +"\" class=\"logo\">\n" +
            "            <p class=\"mailTitle\">账户资金重要通知</p>\n" +
            "        </div>\n" +
            "        <div class=\"mailContent\">\n" +
            "           <p class=\"contentTitle\">亲爱的用户：</p>\n" +
            "           <p class=\"contentInfo\">您的合约已被系统强平。</p>\n" +
            "           <a class=\"btn\" href='"+FORCE_TO_URL+"'>查看详情</a>\n" +
            "        </div>\n" +
            "        <div class=\"mailBottom\">\n" +
            "            <div class=\"mailBottom-left\">\n" +
            "                <p class=\"mailBottom-leftTitle\">本邮件为系统发送，请勿回复。</p>\n" +
            "                <div class=\"mailBottom-leftWrap\">\n" +
            "                    <div class=\"mailBottom-leftContent\">FOTA团队： <span class=\"mailBottom-leftTe\">${sendTime}</span></div>\n" +
            "                    <div class=\"mailBottom-leftContent\">客服邮箱：<span class=\"mailBottom-leftTe\">support@fota.com</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"mailCopy\">\n" +
            "            &copy;©2017-2018 FOTA.com. All Rights Reserved\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

    /**
     * 邮箱中文合约交易-补充USDT
     */
    public static final String EMAIL_CH_SUPPLEMENT_USDT = "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
            "    <title>Document</title>\n" +
            "    <style>\n" +
            "        * {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        .mailWrap {\n" +
            "            width: 640px;\n" +
            "            margin: 0 auto;\n" +
            "        }\n" +
            "        .mailBg {\n" +
            "            width: 100%;\n" +
            "            height: 95px;\n" +
            "            background: url('"+ PICTURE_1 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "        }\n" +
            "        .logo {\n" +
            "            float: left;\n" +
            "            padding-top: 25px;\n" +
            "            padding-left: 40px;\n" +
            "        }\n" +
            "        .mailTitle {\n" +
            "            font-size: 20px;\n" +
            "            color: #fff;\n" +
            "            float: left;\n" +
            "            line-height: 95px;\n" +
            "            margin-left: 152px;\n" +
            "        }\n" +
            "        .mailContent {\n" +
            "            padding: 63px 41px 50px 41px;\n" +
            "        }\n" +
            "        .contentTitle {\n" +
            "            font-size: 18px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 28px;\n" +
            "        }\n" +
            "        .contentInfo {\n" +
            "            margin-top: 30px;\n" +
            "            font-size: 24px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 42px;\n" +
            "        }\n" +
            "        .btn {\n" +
            "            display: block;\n" +
            "            width: 122px;\n" +
            "            height: 42px;\n" +
            "            margin: 0 auto;\n" +
            "            margin-top: 135px;\n" +
            "            background-color: #3d79ec;\n" +
            "\t        border-radius: 21px;\n" +
            "            text-align: center;\n" +
            "            line-height: 42px;\n" +
            "            letter-spacing: 0px;\n" +
            "            color: #ffffff;\n" +
            "            cursor: pointer;\n" +
            "        }\n" +
            "        .mailBottom {\n" +
            "            width: 100%;\n" +
            "            height: 195px;\n" +
            "            padding: 25px 41px 20px 41px;\n" +
            "            background: url('"+ PICTURE_2 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "            border-bottom: 1px solid rgba(213,214,225,0.3);\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftTitle {\n" +
            "            margin-top: 5px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7b7f87;\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftWrap {\n" +
            "            margin-top: 30px;\n" +
            "        }\n" +
            "        .mailBottom-leftContent {\n" +
            "            font-size: 16px;\n" +
            "            line-height: 16px;\n" +
            "            color: #7c8088;\n" +
            "            margin-bottom: 16px;\n" +
            "        }\n" +
            "        .mailBottom-leftTe {\n" +
            "            color: #3d79ec;\n" +
            "        }\n" +
            "        .mailBottom-right {\n" +
            "            float: right;\n" +
            "        }\n" +
            "        .mailCopy {\n" +
            "            width: 100%;\n" +
            "            height: 65px;\n" +
            "            text-align: center;\n" +
            "            line-height: 65px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7c8088;\n" +
            "            background: #fbfbfc;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"mailWrap\">\n" +
            "        <div class=\"mailBg\">\n" +
            "            <img src=\""+ PICTURE_3 +"\" class=\"logo\">\n" +
            "            <p class=\"mailTitle\">账户资金重要通知</p>\n" +
            "        </div>\n" +
            "        <div class=\"mailContent\">\n" +
            "           <p class=\"contentTitle\">亲爱的用户：</p>\n" +
            "           <p class=\"contentInfo\">您的合约账户余额不足，请及时补充USDT，否则您将无法购买合约。</p>\n" +
            "           <a class=\"btn\" href='"+PENETRATE_TO_URL+"'>查看详情</a>\n" +
            "        </div>\n" +
            "        <div class=\"mailBottom\">\n" +
            "            <div class=\"mailBottom-left\">\n" +
            "                <p class=\"mailBottom-leftTitle\">本邮件为系统发送，请勿回复。</p>\n" +
            "                <div class=\"mailBottom-leftWrap\">\n" +
            "                    <div class=\"mailBottom-leftContent\">FOTA团队： <span class=\"mailBottom-leftTe\">${sendTime}</span></div>\n" +
            "                    <div class=\"mailBottom-leftContent\">客服邮箱：<span class=\"mailBottom-leftTe\">support@fota.com</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"mailCopy\">\n" +
            "            &copy;©2017-2018 FOTA.com. All Rights Reserved\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

    /**
     * 邮箱英文验证码
     */
    public static final String EMAIL_EN_CODE = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
            "    <title>Document</title>\n" +
            "    <style>\n" +
            "        * {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        .mailWrap {\n" +
            "            width: 640px;\n" +
            "            margin: 0 auto;\n" +
            "        }\n" +
            "        .mailBg {\n" +
            "            width: 100%;\n" +
            "            height: 95px;\n" +
            "            background: url('"+ PICTURE_1 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "        }\n" +
            "        .logo {\n" +
            "            float: left;\n" +
            "            padding-top: 25px;\n" +
            "            padding-left: 40px;\n" +
            "        }\n" +
            "        .mailTitle {\n" +
            "            font-size: 20px;\n" +
            "            color: #fff;\n" +
            "            float: left;\n" +
            "            line-height: 95px;\n" +
            "            margin-left: 152px;\n" +
            "        }\n" +
            "        .mailContent {\n" +
            "            padding: 63px 41px 50px 41px;\n" +
            "        }\n" +
            "        .contentTitle {\n" +
            "            font-size: 18px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 28px;\n" +
            "        }\n" +
            "        .contentInfo {\n" +
            "            margin-top: 30px;\n" +
            "            font-size: 24px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 42px;\n" +
            "        }\n" +
            "        .btn {\n" +
            "            display: block;\n" +
            "            width: 122px;\n" +
            "            height: 42px;\n" +
            "            margin: 0 auto;\n" +
            "            margin-top: 135px;\n" +
            "            background-color: #3d79ec;\n" +
            "\t        border-radius: 21px;\n" +
            "            text-align: center;\n" +
            "            line-height: 42px;\n" +
            "            letter-spacing: 0px;\n" +
            "            color: #ffffff;\n" +
            "            cursor: pointer;\n" +
            "        }\n" +
            "        .mailBottom {\n" +
            "            width: 100%;\n" +
            "            height: 195px;\n" +
            "            padding: 25px 41px 20px 41px;\n" +
            "            background: url('"+ PICTURE_2 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "            border-bottom: 1px solid rgba(213,214,225,0.3);\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftTitle {\n" +
            "            margin-top: 5px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7b7f87;\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftWrap {\n" +
            "            margin-top: 30px;\n" +
            "        }\n" +
            "        .mailBottom-leftContent {\n" +
            "            font-size: 16px;\n" +
            "            line-height: 16px;\n" +
            "            color: #7c8088;\n" +
            "            margin-bottom: 16px;\n" +
            "        }\n" +
            "        .mailBottom-leftTe {\n" +
            "            color: #3d79ec;\n" +
            "        }\n" +
            "        .mailBottom-right {\n" +
            "            float: right;\n" +
            "        }\n" +
            "        .mailCopy {\n" +
            "            width: 100%;\n" +
            "            height: 65px;\n" +
            "            text-align: center;\n" +
            "            line-height: 65px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7c8088;\n" +
            "            background: #fbfbfc;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"mailWrap\">\n" +
            "        <div class=\"mailBg\">\n" +
            "            <img src=\""+ PICTURE_3 +"\" class=\"logo\">\n" +
            "            <p class=\"mailTitle\">Security Authentication</p>\n" +
            "        </div>\n" +
            "        <div class=\"mailContent\">\n" +
            "           <p class=\"contentTitle\">Dear user,</p>\n" +
            "           <p class=\"contentInfo\">Your verification code is:${code}, it will only be valid for the next 30 minutes. Never disclose this code to anyone, including Fortuna support. If you didn’t request a code, we recommend that you change your password immediately. </p>\n" +
            /*"           <a class=\"btn\">View the details</a>\n" +*/
            "        </div>\n" +
            "        <div class=\"mailBottom\">\n" +
            "            <div class=\"mailBottom-left\">\n" +
            "                <p class=\"mailBottom-leftTitle\">THIS IS AN AUTOMATED MESSAGE - PLEASE DO NOT REPLY DIRECTLY TO THIS EMAIL!</p>\n" +
            "                <div class=\"mailBottom-leftWrap\">\n" +
            "                    <div class=\"mailBottom-leftContent\">FOTA Team: <span class=\"mailBottom-leftTe\">${sendTime}</span></div>\n" +
            "                    <div class=\"mailBottom-leftContent\">Customer service mailbox:<span class=\"mailBottom-leftTe\">support@fota.com</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"mailCopy\">\n" +
            "            &copy;©2017-2018 FOTA.com. All Rights Reserved\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

    /**
     * 邮箱英文合约交易-补充保证金
     */
    public static final String EMAIL_EN_SUPPLEMENT_MARGIN = "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
            "    <title>Document</title>\n" +
            "    <style>\n" +
            "        * {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        .mailWrap {\n" +
            "            width: 640px;\n" +
            "            margin: 0 auto;\n" +
            "        }\n" +
            "        .mailBg {\n" +
            "            width: 100%;\n" +
            "            height: 95px;\n" +
            "            background: url('"+ PICTURE_1 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "        }\n" +
            "        .logo {\n" +
            "            float: left;\n" +
            "            padding-top: 25px;\n" +
            "            padding-left: 40px;\n" +
            "        }\n" +
            "        .mailTitle {\n" +
            "            font-size: 20px;\n" +
            "            color: #fff;\n" +
            "            float: left;\n" +
            "            line-height: 95px;\n" +
            "            margin-left: 152px;\n" +
            "        }\n" +
            "        .mailContent {\n" +
            "            padding: 63px 41px 50px 41px;\n" +
            "        }\n" +
            "        .contentTitle {\n" +
            "            font-size: 18px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 28px;\n" +
            "        }\n" +
            "        .contentInfo {\n" +
            "            margin-top: 30px;\n" +
            "            font-size: 24px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 42px;\n" +
            "        }\n" +
            "        .btn {\n" +
            "            display: block;\n" +
            "            width: 122px;\n" +
            "            height: 42px;\n" +
            "            margin: 0 auto;\n" +
            "            margin-top: 135px;\n" +
            "            background-color: #3d79ec;\n" +
            "\t        border-radius: 21px;\n" +
            "            text-align: center;\n" +
            "            line-height: 42px;\n" +
            "            letter-spacing: 0px;\n" +
            "            color: #ffffff;\n" +
            "            cursor: pointer;\n" +
            "        }\n" +
            "        .mailBottom {\n" +
            "            width: 100%;\n" +
            "            height: 195px;\n" +
            "            padding: 25px 41px 20px 41px;\n" +
            "            background: url('"+ PICTURE_2 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "            border-bottom: 1px solid rgba(213,214,225,0.3);\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftTitle {\n" +
            "            margin-top: 5px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7b7f87;\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftWrap {\n" +
            "            margin-top: 30px;\n" +
            "        }\n" +
            "        .mailBottom-leftContent {\n" +
            "            font-size: 16px;\n" +
            "            line-height: 16px;\n" +
            "            color: #7c8088;\n" +
            "            margin-bottom: 16px;\n" +
            "        }\n" +
            "        .mailBottom-leftTe {\n" +
            "            color: #3d79ec;\n" +
            "        }\n" +
            "        .mailBottom-right {\n" +
            "            float: right;\n" +
            "        }\n" +
            "        .mailCopy {\n" +
            "            width: 100%;\n" +
            "            height: 65px;\n" +
            "            text-align: center;\n" +
            "            line-height: 65px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7c8088;\n" +
            "            background: #fbfbfc;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"mailWrap\">\n" +
            "        <div class=\"mailBg\">\n" +
            "            <img src=\""+ PICTURE_3 +"\" class=\"logo\">\n" +
            "            <p class=\"mailTitle\">Notification Concerning Your Assets</p>\n" +
            "        </div>\n" +
            "        <div class=\"mailContent\">\n" +
            "           <p class=\"contentTitle\">Dear user:</p>\n" +
            "           <p class=\"contentInfo\">Your mortgage rate is too low, please add your margin duly. Otherwise, it will lead to a forced liquidation.</p>\n" +
            "           <a class=\"btn\" href='"+FORCE_TO_URL+"'>View the details</a>\n" +
            "        </div>\n" +
            "        <div class=\"mailBottom\">\n" +
            "            <div class=\"mailBottom-left\">\n" +
            "                <p class=\"mailBottom-leftTitle\">This is an automated message, please do not reply directly to this E-mail!</p>\n" +
            "                <div class=\"mailBottom-leftWrap\">\n" +
            "                    <div class=\"mailBottom-leftContent\">FOTA Team: <span class=\"mailBottom-leftTe\">${sendTime}</span></div>\n" +
            "                    <div class=\"mailBottom-leftContent\">Customer Service Email:<span class=\"mailBottom-leftTe\">support@fota.com</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"mailCopy\">\n" +
            "            &copy;©2017-2018 FOTA.com. All Rights Reserved\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

    /**
     * 邮箱英文合约交易-强平
     */
    public static final String EMAIL_EN_FORCE_QUARE = "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
            "    <title>Document</title>\n" +
            "    <style>\n" +
            "        * {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        .mailWrap {\n" +
            "            width: 640px;\n" +
            "            margin: 0 auto;\n" +
            "        }\n" +
            "        .mailBg {\n" +
            "            width: 100%;\n" +
            "            height: 95px;\n" +
            "            background: url('"+ PICTURE_1 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "        }\n" +
            "        .logo {\n" +
            "            float: left;\n" +
            "            padding-top: 25px;\n" +
            "            padding-left: 40px;\n" +
            "        }\n" +
            "        .mailTitle {\n" +
            "            font-size: 20px;\n" +
            "            color: #fff;\n" +
            "            float: left;\n" +
            "            line-height: 95px;\n" +
            "            margin-left: 152px;\n" +
            "        }\n" +
            "        .mailContent {\n" +
            "            padding: 63px 41px 50px 41px;\n" +
            "        }\n" +
            "        .contentTitle {\n" +
            "            font-size: 18px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 28px;\n" +
            "        }\n" +
            "        .contentInfo {\n" +
            "            margin-top: 30px;\n" +
            "            font-size: 24px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 42px;\n" +
            "        }\n" +
            "        .btn {\n" +
            "            display: block;\n" +
            "            width: 122px;\n" +
            "            height: 42px;\n" +
            "            margin: 0 auto;\n" +
            "            margin-top: 135px;\n" +
            "            background-color: #3d79ec;\n" +
            "\t        border-radius: 21px;\n" +
            "            text-align: center;\n" +
            "            line-height: 42px;\n" +
            "            letter-spacing: 0px;\n" +
            "            color: #ffffff;\n" +
            "            cursor: pointer;\n" +
            "        }\n" +
            "        .mailBottom {\n" +
            "            width: 100%;\n" +
            "            height: 195px;\n" +
            "            padding: 25px 41px 20px 41px;\n" +
            "            background: url('"+ PICTURE_2 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "            border-bottom: 1px solid rgba(213,214,225,0.3);\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftTitle {\n" +
            "            margin-top: 5px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7b7f87;\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftWrap {\n" +
            "            margin-top: 30px;\n" +
            "        }\n" +
            "        .mailBottom-leftContent {\n" +
            "            font-size: 16px;\n" +
            "            line-height: 16px;\n" +
            "            color: #7c8088;\n" +
            "            margin-bottom: 16px;\n" +
            "        }\n" +
            "        .mailBottom-leftTe {\n" +
            "            color: #3d79ec;\n" +
            "        }\n" +
            "        .mailBottom-right {\n" +
            "            float: right;\n" +
            "        }\n" +
            "        .mailCopy {\n" +
            "            width: 100%;\n" +
            "            height: 65px;\n" +
            "            text-align: center;\n" +
            "            line-height: 65px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7c8088;\n" +
            "            background: #fbfbfc;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"mailWrap\">\n" +
            "        <div class=\"mailBg\">\n" +
            "            <img src=\""+ PICTURE_3 +"\" class=\"logo\">\n" +
            "            <p class=\"mailTitle\">Notification Concerning Your Assets</p>\n" +
            "        </div>\n" +
            "        <div class=\"mailContent\">\n" +
            "           <p class=\"contentTitle\">Dear user:</p>\n" +
            "           <p class=\"contentInfo\">Your contract has been liquidated forcefully by our system.</p>\n" +
            "           <a class=\"btn\" href='"+FORCE_TO_URL+"'>View the details</a>\n" +
            "        </div>\n" +
            "        <div class=\"mailBottom\">\n" +
            "            <div class=\"mailBottom-left\">\n" +
            "                <p class=\"mailBottom-leftTitle\">This is an automated message, please do not reply directly to this E-mail!</p>\n" +
            "                <div class=\"mailBottom-leftWrap\">\n" +
            "                    <div class=\"mailBottom-leftContent\">FOTA Team: <span class=\"mailBottom-leftTe\">${sendTime}</span></div>\n" +
            "                    <div class=\"mailBottom-leftContent\">Customer Service Email:<span class=\"mailBottom-leftTe\">support@fota.com</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"mailCopy\">\n" +
            "            &copy;©2017-2018 FOTA.com. All Rights Reserved\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";

    /**
     * 邮箱英文合约交易-补充USDT
     */
    public static final String EMAIL_EN_SUPPLEMENT_USDT = "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
            "    <title>Document</title>\n" +
            "    <style>\n" +
            "        * {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        .mailWrap {\n" +
            "            width: 640px;\n" +
            "            margin: 0 auto;\n" +
            "        }\n" +
            "        .mailBg {\n" +
            "            width: 100%;\n" +
            "            height: 95px;\n" +
            "            background: url('"+ PICTURE_1 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "        }\n" +
            "        .logo {\n" +
            "            float: left;\n" +
            "            padding-top: 25px;\n" +
            "            padding-left: 40px;\n" +
            "        }\n" +
            "        .mailTitle {\n" +
            "            font-size: 20px;\n" +
            "            color: #fff;\n" +
            "            float: left;\n" +
            "            line-height: 95px;\n" +
            "            margin-left: 152px;\n" +
            "        }\n" +
            "        .mailContent {\n" +
            "            padding: 63px 41px 50px 41px;\n" +
            "        }\n" +
            "        .contentTitle {\n" +
            "            font-size: 18px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 28px;\n" +
            "        }\n" +
            "        .contentInfo {\n" +
            "            margin-top: 30px;\n" +
            "            font-size: 24px;\n" +
            "            color: #1b1d34;\n" +
            "            line-height: 42px;\n" +
            "        }\n" +
            "        .btn {\n" +
            "            display: block;\n" +
            "            width: 122px;\n" +
            "            height: 42px;\n" +
            "            margin: 0 auto;\n" +
            "            margin-top: 135px;\n" +
            "            background-color: #3d79ec;\n" +
            "\t        border-radius: 21px;\n" +
            "            text-align: center;\n" +
            "            line-height: 42px;\n" +
            "            letter-spacing: 0px;\n" +
            "            color: #ffffff;\n" +
            "            cursor: pointer;\n" +
            "        }\n" +
            "        .mailBottom {\n" +
            "            width: 100%;\n" +
            "            height: 195px;\n" +
            "            padding: 25px 41px 20px 41px;\n" +
            "            background: url('"+ PICTURE_2 +"') no-repeat;\n" +
            "            background-size: cover;\n" +
            "            border-bottom: 1px solid rgba(213,214,225,0.3);\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftTitle {\n" +
            "            margin-top: 5px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7b7f87;\n" +
            "        }\n" +
            "        .mailBottom-left {\n" +
            "            float: left;\n" +
            "        }\n" +
            "        .mailBottom-leftWrap {\n" +
            "            margin-top: 30px;\n" +
            "        }\n" +
            "        .mailBottom-leftContent {\n" +
            "            font-size: 16px;\n" +
            "            line-height: 16px;\n" +
            "            color: #7c8088;\n" +
            "            margin-bottom: 16px;\n" +
            "        }\n" +
            "        .mailBottom-leftTe {\n" +
            "            color: #3d79ec;\n" +
            "        }\n" +
            "        .mailBottom-right {\n" +
            "            float: right;\n" +
            "        }\n" +
            "        .mailCopy {\n" +
            "            width: 100%;\n" +
            "            height: 65px;\n" +
            "            text-align: center;\n" +
            "            line-height: 65px;\n" +
            "            font-size: 16px;\n" +
            "            color: #7c8088;\n" +
            "            background: #fbfbfc;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"mailWrap\">\n" +
            "        <div class=\"mailBg\">\n" +
            "            <img src=\""+ PICTURE_3 +"\" class=\"logo\">\n" +
            "            <p class=\"mailTitle\">Notification Concerning Your Assets</p>\n" +
            "        </div>\n" +
            "        <div class=\"mailContent\">\n" +
            "           <p class=\"contentTitle\">Dear user:</p>\n" +
            "           <p class=\"contentInfo\">Insufficient balance for your futures account, please deposit USDT duly. Or you will unable to buy contracts anymore.</p>\n" +
            "           <a class=\"btn\" href='"+PENETRATE_TO_URL+"'>View the details</a>\n" +
            "        </div>\n" +
            "        <div class=\"mailBottom\">\n" +
            "            <div class=\"mailBottom-left\">\n" +
            "                <p class=\"mailBottom-leftTitle\">This is an automated message, please do not reply directly to this E-mail!</p>\n" +
            "                <div class=\"mailBottom-leftWrap\">\n" +
            "                    <div class=\"mailBottom-leftContent\">FOTA Team: <span class=\"mailBottom-leftTe\">${sendTime}</span></div>\n" +
            "                    <div class=\"mailBottom-leftContent\">Customer Service Email:<span class=\"mailBottom-leftTe\">support@fota.com</span></div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"mailCopy\">\n" +
            "            &copy;©2017-2018 FOTA.com. All Rights Reserved\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
}