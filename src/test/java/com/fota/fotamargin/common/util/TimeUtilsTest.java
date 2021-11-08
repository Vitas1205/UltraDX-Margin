//package com.fota.fotamargin.common.util;
//
//import com.fota.fotamargin.common.enums.ContractTypeEnum;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Calendar;
//
//import static org.junit.Assert.*;
//
///**
// * @author taoyuanming
// * Created on 2018/8/8
// * Description
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class TimeUtilsTest {
//
//    @Test
//    public void calculateNextDeliveryMillis() {
//        Calendar calendar = Calendar.getInstance();
//        long lll = calendar.getTimeInMillis();
//
//        for (int i = 0; i < 15; i++) {
//            lll = TimeUtils.calculateNextDeliveryMillis(lll, 3);
//            System.out.println(TimeUtils.formatMillisecondsToString(lll));
//        }
//
//    }
//}