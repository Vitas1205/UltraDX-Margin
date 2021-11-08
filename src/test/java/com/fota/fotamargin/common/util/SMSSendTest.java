//package com.fota.fotamargin.common.util;
//
//import com.fota.account.domain.UserBaseDTO;
//import com.fota.account.service.UserBaseService;
//import com.fota.fotamargin.common.util.email.EmailSend;
//import com.fota.fotamargin.common.util.email.MailOperatorEnum;
//import com.fota.fotamargin.common.util.phone.ErrorEnum;
//import com.fota.fotamargin.common.util.phone.Result;
//import com.fota.fotamargin.common.util.phone.SMSSend;
//import org.apache.commons.lang.StringUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * @author taoyuanming
// * Created on 2018/8/15
// * Description
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class SMSSendTest {
//    private static final Logger LOGGER = LoggerFactory.getLogger(SMSSendTest.class);
//
//    @Autowired
//    private UserBaseService userBaseService;
//
//    @Autowired
//    private EmailSend emailSend;
//
//    @Test
//    public void sendSMS() {
//        String code = CodeRandomUtil.getCode();
//        UserBaseDTO userBaseDTO = userBaseService.getUserBaseInfoByUserId(0);
//        Result result = SMSSend.sendSMS(userBaseDTO.getPhoneCountryCode(), userBaseDTO.getPhone(), Integer.valueOf(code), 10, 113304);
//    }
//
//    @Test
//    public void sendMail() {
//        UserBaseDTO userBaseDTO = userBaseService.getUserBaseInfoByUserId(17764593889L);
//        MailOperatorEnum mailOperatorEnum = MailOperatorEnum.getMailOperatorEnumByCode(1);
//        if (mailOperatorEnum == null) {
//            LOGGER.error("verifyType is not defined");
//        }
//        String email = userBaseDTO.getEmail();
//        if (StringUtils.isBlank(email)) {
//            LOGGER.error("email is empty");
//        }
//        String code = CodeRandomUtil.getCode();
//        int i = emailSend.sendMail(email, code, mailOperatorEnum);
//        if (i == 0) {
//            LOGGER.info("send email code :{}", code);
//        }
//    }
//
//}