//package com.fota.fotamargin.test;
//
//import com.fota.asset.domain.UserContractDTO;
//import com.fota.asset.service.AssetService;
//import com.fota.fotamargin.common.util.FUUID;
//import com.fota.fotamargin.common.util.TimeUtils;
//import com.fota.fotamargin.manager.RocketMqManager;
//import com.fota.margin.domain.MarginMqDto;
//import com.fota.margin.domain.MqConstant;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//
///**
// * @author taoyuanming
// * Created on 2018/7/9
// * Description 15分K线初始化
// */
//
////@Component
////@EnableScheduling
//public class MQTestTask {
//
//
//    @Autowired
//    private RocketMqManager rocketMqManager;
//
//    @Autowired
//    private AssetService assetService;
//
////    @Scheduled(fixedDelay = 10000)
//    public void execute() {
//
//
//        //获取所有合约账户
//        List<UserContractDTO> allUserContractDTOList = null;
//        try {
//            allUserContractDTOList = assetService.getAllContractAccount();
//        } catch (Exception e) {
//        }
//        if (CollectionUtils.isNotEmpty(allUserContractDTOList)) {
//            for (UserContractDTO userContractDTO : allUserContractDTOList) {
//                rocketMqManager.sendMessage(MqConstant.MARGIN_TOPIC, MqConstant.FORCE_WARN, new MarginMqDto(MqConstant.TYPE_FORCE, userContractDTO.getUserId(), TimeUtils.getTimeInMillis(), FUUID.getUUUID()));
//
//            }
//        }
//
//
//    }
//}
