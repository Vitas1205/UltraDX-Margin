package com.fota.fotamargin.manager;

import com.alibaba.fastjson.JSONObject;
import com.fota.fotamargin.manager.mq.RocketMqProducer;
import com.fota.margin.domain.MarginMqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author taoyuanming
 * Created on 2018/8/9
 * Description  RocketMqManager
 */
@Component
@Slf4j
public class RocketMqManager {

    @Autowired
    private RocketMqProducer rocketMqProducer;

    public void sendMessage(String topic, String tag, MarginMqDto message){
        rocketMqProducer.producer(topic, tag, message.toString(), JSONObject.toJSONString(message));
    }
}
