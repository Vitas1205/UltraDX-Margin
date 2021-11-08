package com.fota.fotamargin.manager.mq;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author taoyuanming
 * Created on 2018/8/9
 * Description  RocketMqProducer
 */
@Slf4j
@Data
@Component
public class RocketMqProducer {

    @Autowired
    private DefaultMQProducer producer;

    /**
     * 生成消息的公共方法,topic、tag、pushMsg不能为空
     * @param topic
     * @param tag
     * @param key 唯一标识码，代表这条消息的业务关键词，服务器会根据keys创建哈希索引，设置后，可以在Console系统根据Topic、Keys来查询消息，
     *            由于是哈希索引，请尽可能保证key唯一，例如订单号，商品Id等
     * @param pushMsg
     * @return
     */
    public void producer(String topic, String tag, String key, String pushMsg){
        SendResult result;
        try {
            Message msg = new Message(topic,tag,key, pushMsg.getBytes("UTF-8"));
            // 消息在1S内没有发送成功，就会重试
            result = producer.send(msg, 1000);
            log.info("RocketMqProducer>>success, topic:{}, tag:{}, pushMsg:{}, SendStatus:{}", topic, tag, pushMsg, result.getSendStatus().toString());
        } catch (Exception e) {
            log.error("RocketMqProducer>>exception, topic:{}, tag:{}, pushMsg:{}", topic, tag, pushMsg);
            log.error("RocketMqProducer>>exception:", e);
        }
    }
}
