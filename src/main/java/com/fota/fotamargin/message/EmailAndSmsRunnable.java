package com.fota.fotamargin.message;

import com.fota.fotamargin.common.util.BeanUtil;
import com.fota.fotamargin.manager.MarginManager;
import com.fota.margin.domain.NotifyDataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuanming Tao
 * Created on 2018/11/13
 * Description
 */
public class EmailAndSmsRunnable implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqRunnable.class);

    private static MarginManager marginManager = BeanUtil.getBean(MarginManager.class);

    private Long userId;
    private Integer type;
    private NotifyDataBase notifyData;

    public EmailAndSmsRunnable(Long userId, Integer type) {
        this.userId = userId;
        this.type = type;
    }

    public EmailAndSmsRunnable(Long userId, Integer type, NotifyDataBase notifyData) {
        this.userId = userId;
        this.type = type;
        this.notifyData = notifyData;
    }

    @Override
    public void run() {
        try {
            marginManager.sendEmailAndSms(userId, type, notifyData);
        } catch (Exception e) {
            LOGGER.error("marginManager.sendEmailAndSms Exception:", e);
        }
    }
}
