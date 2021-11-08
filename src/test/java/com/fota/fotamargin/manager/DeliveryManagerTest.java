//package com.fota.fotamargin.manager;
//
//import com.fota.fotamargin.dao.domain.MarginDealRecordDO;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.math.BigDecimal;
//
//import static org.junit.Assert.*;
//
///**
// * @author taoyuanming
// * Created on 2018/8/8
// * Description
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class DeliveryManagerTest {
//
//    @Autowired
//    private DeliveryManager deliveryManager;
//
//    @Test
//    public void save() {
//        MarginDealRecordDO marginDealRecordDO = new MarginDealRecordDO();
//        marginDealRecordDO.setAmount(new BigDecimal(1));
//        deliveryManager.save(marginDealRecordDO);
//    }
//}