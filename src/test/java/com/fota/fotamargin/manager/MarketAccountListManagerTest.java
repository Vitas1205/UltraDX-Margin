package com.fota.fotamargin.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Yuanming Tao
 * Created on 2018/11/29
 * Description
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarketAccountListManagerTest {

    @Autowired
    private MarketAccountListManager marketAccountListManager;

    @Test
    public void contains() {
        boolean b = marketAccountListManager.contains(17764594947L);
        System.out.println(b);
    }
}