//package com.fota.fotamargin.thread;
//
//import com.alibaba.fastjson.JSON;
//import com.fota.asset.domain.UserContractDTO;
//import com.fota.ticker.entrust.RealTimeEntrust;
//import com.fota.ticker.entrust.entity.CompetitorsPriceDTO;
//import com.fota.trade.domain.ContractCategoryDTO;
//import com.fota.trade.domain.ContractOrderDTO;
//import com.fota.trade.domain.UserPositionDTO;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.CountDownLatch;
//
///**
// * @author taoyuanming
// * Created on 2018/8/5
// * Description
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class BatchForcedLiquidationRunnerTest {
//
//    @Autowired
//    private RealTimeEntrust realTimeEntrust;
//
//    @Test
//    public void filterOrder() throws Exception {
//
//        List<UserContractDTO> userContractDTOList = null;
//        CountDownLatch countDownLatch = null;
//        Map<String, BigDecimal> latestPriceMap = null;
//        Map<Long, ContractCategoryDTO> contractCategoryDTOMap = null;
//        BatchForcedLiquidationRunner batchForcedLiquidationRunner = new BatchForcedLiquidationRunner(userContractDTOList, countDownLatch, latestPriceMap, contractCategoryDTOMap);
//
//        List<ContractOrderDTO> orderDTOList = new ArrayList<>();
//        ContractOrderDTO dto = new ContractOrderDTO();
//        dto.setUnfilledAmount(2L);
//        dto.setPrice(new BigDecimal("5.1"));
//        orderDTOList.add(dto);
//
//        dto = new ContractOrderDTO();
//        dto.setUnfilledAmount(1L);
//        dto.setPrice(new BigDecimal("5.7"));
//        orderDTOList.add(dto);
//
//        dto = new ContractOrderDTO();
//        dto.setUnfilledAmount(3L);
//        dto.setPrice(new BigDecimal("4.9"));
//        orderDTOList.add(dto);
//
//        UserPositionDTO positionDTO = new UserPositionDTO();
//        positionDTO.setAmount(5L);
//
//        BigDecimal latestPrice = new BigDecimal(5.2);
//        batchForcedLiquidationRunner.filterOrder(orderDTOList, positionDTO);
//    }
//
//    @Test
//    public void getCompetitorsPrice() {
//        List<CompetitorsPriceDTO> competitorsPriceDTOList = realTimeEntrust.getCompetitorsPrice(2);
//        System.out.println(JSON.toJSONString(competitorsPriceDTOList));
//    }
//
//    @Test
//    public void singleForcedOrder() {
//        List<UserContractDTO> userContractDTOList = null;
//        CountDownLatch countDownLatch = null;
//        Map<String, BigDecimal> latestPriceMap = null;
//        Map<Long, ContractCategoryDTO> contractCategoryDTOMap = null;
//        BatchForcedLiquidationRunner batchForcedLiquidationRunner = new BatchForcedLiquidationRunner(userContractDTOList, countDownLatch, latestPriceMap, contractCategoryDTOMap);
//
//        UserPositionDTO userPositionDTO = new UserPositionDTO();
//        userPositionDTO.setUserId(6666666L);
//        userPositionDTO.setContractId(6666666L);
//        userPositionDTO.setContractName("margin_test");
//        userPositionDTO.setPositionType(2);
//        userPositionDTO.setAmount(666666L);
//
//        batchForcedLiquidationRunner.singleForcedOrder(userPositionDTO);
//
//    }
//}