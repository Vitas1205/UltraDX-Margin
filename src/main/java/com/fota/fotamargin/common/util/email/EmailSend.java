package com.fota.fotamargin.common.util.email;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fota.common.enums.InformEnum;
import com.fota.common.utils.InformUtil;
import com.fota.fotamargin.exception.MarginException;
import com.fota.margin.domain.ForceNotifyData;
import com.fota.margin.domain.MarginForceOrder;
import com.fota.margin.domain.NotifyDataBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author jintian on 2018/6/24.
 * @see
 */

@Slf4j
@Service
public class EmailSend {
    @Value("${fota.mail_access_id}")
    private String ACCESSID;

    /**
     * Access Key Secret
     */
    @Value("${fota.mail_access_key}")
    private String SECRET;

    @Value("${fota.mail_sender:no-reply@mail.fota.com}")
    private String sender;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSend.class);


    /**
     * send Email, return 1 if succeed, return 0 if failed.
     */
    public void sendMail(String address, InformEnum informEnum, Integer language, NotifyDataBase notifyData) throws Exception {
        language = Objects.isNull(language)? 1: language;
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSID, SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        String msg;
        if (InformEnum.FORCE_QUARE.equals(informEnum)) {
            if (notifyData == null) {
                throw new MarginException("notifyData is empty!");
            }
            ForceNotifyData forceNotifyData = (ForceNotifyData) notifyData;
            List<MarginForceOrder> marginForceOrders = forceNotifyData.getMarginForceOrders();
            if (CollectionUtils.isEmpty(marginForceOrders)) {
                throw new MarginException("marginForceOrders is empty!");
            }
            StringBuilder subContent = new StringBuilder();
            for (MarginForceOrder marginForceOrder : marginForceOrders) {
                subContent.append(informEnum.getEmailSubTemplate()[language - 1].replace("${contractName}", marginForceOrder.getContractName()).replace("${orderDirection}", InformUtil.ORDER_DIRECTION_ARR[language - 1][marginForceOrder.getOrderDirection() - 1]).replace("${price}", marginForceOrder.getPrice() + "").replace("${value}", marginForceOrder.getValue() + ""));
            }
            msg = informEnum.getEmailTemplate()[language - 1].replace("${forceTime}", DateUtil.formatToString(new Date(forceNotifyData.getForceTime()), "yyyy.MM.dd HH:mm:ss")).replace("${subContent}", subContent).replace("${sendTime}", DateUtil.formatToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        } else {
            msg = informEnum.getEmailTemplate()[language - 1].replace("${sendTime}", DateUtil.formatToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
        SingleSendMailRequest request = buildMailRequest(address, msg, informEnum.getTitles()[language - 1]);
        SingleSendMailResponse httpResponse = client.getAcsResponse(request);
        httpResponse.getRequestId();
    }
//
//    /**
//     * send Email, return 1 if succeed, return 0 if failed.
//     */
//    public int sendMail(String address, MailOperatorEnum operator, Integer language) {
//        log.info("sendMail method. address:{}, operate:{}", address, operator.getText());
//        language = Objects.isNull(language)? 1: language;
//        try {
//            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSID, SECRET);
//            IAcsClient client = new DefaultAcsClient(profile);
//            SingleSendMailRequest request = buildMailRequest(address, MailOperatorEnum.getMsg(operator, language), MailOperatorEnum.getTitle(operator, language));
//            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
//            httpResponse.getRequestId();
//            return 1;
//        } catch (Exception e) {
//            LOGGER.error("sendMail method Exception. address:{}, operator:{}", address, operator, e);
//        }
//        return 0;
//    }
//
//    public int sendCodeMail(String address, String code, MailOperatorEnum operator, Integer language) {
//        log.info("sendCodeMail method. address:{}, code:{}, operate:{}", address, code, operator.getText());
//        language = Objects.isNull(language)? 1: language;
//        try {
//            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSID, SECRET);
//            IAcsClient client = new DefaultAcsClient(profile);
//            SingleSendMailRequest request = buildMailRequest(address,
//                    MailOperatorEnum.getMsg(operator, language).replace("${code}", code), MailOperatorEnum.getTitle(operator, language));
//            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
//            httpResponse.getRequestId();
//            return 1;
//        } catch (Exception e) {
//            LOGGER.error("sendCodeMail method Exception. address:{}, operator:{}", address, operator, e);
//        }
//        return 0;
//    }

    private SingleSendMailRequest buildMailRequest(String address, String msg, String title) {
        SingleSendMailRequest request = new SingleSendMailRequest();
        sendCouponCodeMail(request, msg, title);
        request.setAccountName(sender);
        request.setFromAlias("FOTA");
        request.setAddressType(1);
        request.setTagName("null");
        request.setReplyToAddress(false);
        request.setToAddress(address);
        return request;
    }

    private void sendCouponCodeMail(SingleSendMailRequest request, String msg, String title) {
        request.setHtmlBody(msg);
        request.setSubject(title);
    }
}
