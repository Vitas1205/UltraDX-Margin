//package com.fota.fotamargin.manager;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.Assert.*;
//
///**
// * @author taoyuanming
// * Created on 2018/8/17
// * Description
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class RedisManagerTest {
//
//    @Autowired
//    private RedisManager redisManager;
//
//    @Test
//    public void sAdd() {
//        Long l = 18L;
//        Long ll = redisManager.sAdd("zlb01", String.valueOf(l));
//        System.out.println(ll);
//    }
//
//    @Test
//    public void sRem() {
//        Long l = 19L;
//        Boolean b = redisManager.sRem("zlb01", String.valueOf(l));
//        System.out.println(b);
//    }
//
//    @Test
//    public void del() {
//        redisManager.deleteValue("zlb01");
//    }
//}