package com.fota.fotamargin.message;

import com.fota.fotamargin.common.util.BeanUtil;
import com.fota.fotamargin.manager.RocketMqManager;
import com.fota.margin.domain.MarginMqDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuanming Tao
 * Created on 2018/11/13
 * Description
 */
public class MqRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqRunnable.class);

    private static RocketMqManager rocketMqManager = BeanUtil.getBean(RocketMqManager.class);
    private String topic;
    private String tag;
    private MarginMqDto message;

    public MqRunnable(String topic, String tag, MarginMqDto message) {
        this.topic = topic;
        this.tag = tag;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            rocketMqManager.sendMessage(topic, tag, message);
        } catch (Exception e) {
            LOGGER.error("RocketMqProducer>>MqRunnable>>Exception:", e);
        }
    }
}
