package com.fota.fotamargin.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
/**
 * @author Tao Yuanming
 * Created on 2018/9/5
 * Description
 */
@Configuration
@RefreshScope
public class PropertiesConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesConfig.class);

    @Value("${spring.application.name}")
    private String name;

    @Value("${SPRING_SERVICE:dev}")
    private String profile;

    @Value("${fota.mail.template.url}")
    private String url;

    @Value("${fota.mail.template.picture_a}")
    private String pictureA;

    @Value("${fota.mail.template.picture_b}")
    private String pictureB;

    @Value("${fota.mail.template.picture_c}")
    private String pictureC;

    @Value("${fota.mail.template.picture_d}")
    private String pictureD;

    @Value("${spring.cloud.zookeeper.connect-string}")
    private String zkServerLists;

    public String getZkServerLists() {
        return zkServerLists;
    }

    public String getNamespace() {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(profile)) {
            LOGGER.error("PropertiesConfig getNamespace error, name:{}, profile:{}", name, profile);
        }
        return name + "-" + profile;
    }

    public String getUrl() {
        return url;
    }

    public String getPictureA() {
        return pictureA;
    }

    public String getPictureB() {
        return pictureB;
    }

    public String getPictureC() {
        return pictureC;
    }

    public String getPictureD() {
        return pictureD;
    }
}
