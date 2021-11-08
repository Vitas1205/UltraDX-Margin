package com.fota.fotamargin.manager;

import com.alibaba.fastjson.JSON;
import com.fota.account.domain.UserBaseDTO;
import com.fota.account.service.UserBaseService;
import com.fota.asset.domain.UserContractDTO;
import com.fota.asset.service.AssetService;
import com.fota.common.enums.InformEnum;
import com.fota.data.domain.TickerDTO;
import com.fota.data.manager.IndexCacheManager;
import com.fota.data.service.DeliveryIndexService;
import com.fota.data.service.SpotIndexService;
import com.fota.fotamargin.common.entity.InternalMethodResult;
import com.fota.fotamargin.common.enums.CoinSymbolEnum;
import com.fota.fotamargin.common.enums.PositionTypeEnum;
import com.fota.fotamargin.common.enums.PriceDirectionEnum;
import com.fota.fotamargin.common.util.TimeUtils;
import com.fota.fotamargin.common.util.email.EmailSend;
import com.fota.fotamargin.common.util.email.LanguageEnum;
import com.fota.fotamargin.common.util.phone.SMSSend;
import com.fota.fotamargin.manager.log.ForcedLog;
import com.fota.fotamargin.manager.trade.ContractCategoryManager;
import com.fota.margin.domain.CommonConstant;
import com.fota.margin.domain.MqConstant;
import com.fota.margin.domain.NotifyDataBase;
import com.fota.policy.service.OperationLimitService;
import com.fota.ticker.entrust.RealTimeEntrust;
import com.fota.ticker.entrust.entity.CompetitorsPriceDTO;
import com.fota.trade.domain.*;
import com.fota.trade.domain.enums.ContractStatus;
import com.fota.trade.service.ContractCategoryService;
import com.fota.trade.service.ContractOrderService;
import com.fota.trade.service.UserContractLeverService;
import com.fota.trade.service.UserPositionService;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.fota.fotamargin.common.constant.MarginConstant.SCALE;

/**
 * @author taoyuanming
 * Created on 2018/8/11
 * Description 保证金
 */
@Service
public class MarginManager {
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 实时现货指数无效的标的物和无效开始时间的映射关系集合
     */
    private static Map<String,Long> invalidIndexAssetMap = new ConcurrentHashMap<>();

    private static List<String> contractAssetList = Arrays.asList(CoinSymbolEnum.BTC.getName(), CoinSymbolEnum.ETH.getName());

    /**
     * 当前交易合约的标的物数量
     */
    private static Integer count = contractAssetList.size();

    @Autowired
    private UserContractLeverService userContractLeverService;

    @Autowired
    private UserPositionService userPositionService;

    @Autowired
    private ContractCategoryService contractCategoryService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserBaseService userBaseService;

    @Autowired
    private EmailSend emailSend;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private ContractOrderService contractOrderService;

    @Autowired
    private RealTimeEntrust realTimeEntrust;

    @Autowired
    private DeliveryIndexService deliveryIndexService;

    @Autowired
    private SpotIndexService spotIndexService;

    @Autowired
    private IndexCacheManager indexCacheManager;

    @Autowired
    private ContractCategoryManager contractCategoryManager;

    @Autowired
    private UserPositionManager userPositionManager;

    /**
     * 计算合约账户总权益
     * @param userId
     * @return
     */
    public InternalMethodResult calculateUserRights(Long userId, Map<String, BigDecimal> latestPriceMap, Set<Long> deliveringIdSet) {
        UserContractDTO contractUser = assetService.getContractAccount(userId);
        if (contractUser == null) {
            return InternalMethodResult.error("contractUser is null!");
        }
//
//        //获取合约最新价
//        List<CompetitorsPriceDTO> competitorsPriceDTOList = realTimeEntrust.getContractCompetitorsPrice();
//        if (CollectionUtils.isEmpty(competitorsPriceDTOList)) {
//            return InternalMethodResult.error("competitorsPriceDTOList is empty!");
//        }
//
//        Map<String, BigDecimal> latestPriceMap = new HashMap<>();
//        for (CompetitorsPriceDTO competitorsPriceDTO : competitorsPriceDTOList) {
//            //type=2:合约 id:合约id orderDirection:1卖 2买
//            if (com.fota.ticker.entrust.entity.enums.OrderDirectionEnum.SELL.getCode() == competitorsPriceDTO.getOrderDirection()) {
//                latestPriceMap.put(competitorsPriceDTO.getId() + com.fota.ticker.entrust.entity.enums.OrderDirectionEnum.SELL.getName(), competitorsPriceDTO.getPrice());
//            } else if (com.fota.ticker.entrust.entity.enums.OrderDirectionEnum.BUY.getCode() == competitorsPriceDTO.getOrderDirection()){
//                latestPriceMap.put(competitorsPriceDTO.getId() + com.fota.ticker.entrust.entity.enums.OrderDirectionEnum.BUY.getName(), competitorsPriceDTO.getPrice());
//            }
//        }

//        for (ContractCategoryDTO contractCategoryDTO : contractCategoryDTOList) {
//            ContractCategoryMap.put(contractCategoryDTO.getId(), contractCategoryDTO);
//        }

        //获取用户自选的合约标的物杠杆倍数
//        Map<Integer, Integer> assetIdLeverMap = getAssetIdLeverMap(userId);
//        if (MapUtils.isEmpty(assetIdLeverMap)) {
//            return InternalMethodResult.error("assetIdLeverMap is empty!");
//        }

        //获取用户所有持仓
        List<UserPositionDTO> userPositionDTOList = this.listPositionByUserId(userId, deliveringIdSet);
        if (CollectionUtils.isEmpty(userPositionDTOList)) {
            return InternalMethodResult.success(new BigDecimal(contractUser.getAmount()));
        }

//        BigDecimal positionMargin = new BigDecimal(0);
        BigDecimal positionProfit = new BigDecimal(0);
        BigDecimal latestPrice;
        for (UserPositionDTO userPositionDTO : userPositionDTOList) {
            if (PositionTypeEnum.OVER.getCode().equals(userPositionDTO.getPositionType())) {
                // 获取最新买一价
                latestPrice = latestPriceMap.get(userPositionDTO.getContractId() + PriceDirectionEnum.BUY.getName());
            } else {
                // 获取最新卖一价
                latestPrice = latestPriceMap.get(userPositionDTO.getContractId() + PriceDirectionEnum.SELL.getName());
            }
//            positionMargin = positionMargin.add(calculatePositionMargin(userPositionDTO, assetIdLeverMap.get(ContractCategoryMap.get(userPositionDTO.getContractId()).getAssetId()), latestPrice));

            positionProfit = positionProfit.add(calculatePositionProfit(userPositionDTO, latestPrice));
        }

        BigDecimal userTotalRights = new BigDecimal(contractUser.getAmount()).add(positionProfit);
        return InternalMethodResult.success(userTotalRights);
    }

    /**
     * 计算持仓保证金
     *
     * @param userPositionDTO
     * @return
     */
    public BigDecimal calculatePositionMargin(UserPositionDTO userPositionDTO, Integer customLeverage, BigDecimal latestPrice) {
        return calculateSinglePositionValue(userPositionDTO, latestPrice).divide(new BigDecimal(customLeverage), SCALE, RoundingMode.HALF_UP);
    }


    /**
     * 计算单仓位价值
     * @param userPositionDTO
     * @return
     */
    public BigDecimal calculateSinglePositionValue(UserPositionDTO userPositionDTO, BigDecimal latestPrice) {
        //小数位的确定
        //仓位价值计算 多仓价值 = |多仓合约份量| * 当前买一价格 * 合约大小；空仓价值 = |空仓合约份量| * 当前卖一价格 * 合约大小
//        return userPositionDTO.getContractSize().multiply(userPositionDTO.getAmount().abs()).multiply(latestPrice);
        return userPositionDTO.getAmount().abs().multiply(latestPrice);
    }

    /**
     * 获取用户自选的合约标的物杠杆倍数
     * @param userId
     */
    public Map<Integer, Integer> getAssetIdLeverMap(Long userId) {
        Map<Integer, Integer> assetIdLeverMap = new HashMap<>();
        List<UserContractLeverDTO> userContractLeverDTOList = userContractLeverService.listUserContractLever(userId);
        if (CollectionUtils.isEmpty(userContractLeverDTOList)) {
            log.error("userContractLeverDTOList is empty! userId:{}", userId);
            return assetIdLeverMap;
        }
        for (UserContractLeverDTO userContractLeverDTO : userContractLeverDTOList) {
            assetIdLeverMap.put(userContractLeverDTO.getAssetId(), userContractLeverDTO.getLever());
        }
        return assetIdLeverMap;
    }

    /**
     * 计算持仓浮盈亏
     *
     * @param dto
     * @return
     */
    public BigDecimal calculatePositionProfit(UserPositionDTO dto, BigDecimal latestPrice) {
        BigDecimal profit;
        if (PositionTypeEnum.OVER.getCode().equals(dto.getPositionType())) {
//            profit = latestPrice.subtract(new BigDecimal(dto.getAveragePrice())).multiply(dto.getAmount()).multiply(dto.getContractSize());
            profit = latestPrice.subtract(new BigDecimal(dto.getAveragePrice())).multiply(dto.getAmount());
        } else {
//            profit = new BigDecimal(dto.getAveragePrice()).subtract(latestPrice).multiply(dto.getAmount()).multiply(dto.getContractSize());
            profit = new BigDecimal(dto.getAveragePrice()).subtract(latestPrice).multiply(dto.getAmount());

        }
        return profit;
    }

    public void sendEmailAndSms(Long userId, Integer type, NotifyDataBase notifyData) {
        try {
            sendSMS(userId, type);
        } catch (Exception e) {
            log.error("sendSMS>>error>>userId:{}, type:{}, e:", userId, type, e);
        }

        try {
            sendMail(userId, type, notifyData);
        } catch (Exception e) {
            log.info("sendMail>>error>>userId:{}, type:{}. e:", userId, type, e);
        }
    }

    public void sendSMS(Long userId, Integer type) throws Exception {
        UserBaseDTO userBaseDTO = userBaseService.getUserBaseInfoByUserId(userId);
        if (StringUtils.isBlank(userBaseDTO.getPhoneCountryCode()) || StringUtils.isBlank(userBaseDTO.getPhone())) {
            log.info("sendSMS>>not binding phone, userId:{}, type:{}", userId, type);
            return;
        }
        Integer lang = com.fota.common.enums.LanguageEnum.convertToNotifyLang(userBaseDTO.getLang());
        Integer templateId;
        if (MqConstant.TYPE_MARGIN.equals(type)) {
            templateId = InformEnum.SUPPLEMENT_MARGIN.getTemplateIds()[lang - 1];
        } else if (MqConstant.TYPE_FORCE.equals(type)) {
            templateId = InformEnum.FORCE_QUARE.getTemplateIds()[lang - 1];
        } else if (MqConstant.TYPE_PENETRATE.equals(type)) {
            templateId = InformEnum.SUPPLEMENT_BTC.getTemplateIds()[lang - 1];
        } else {
            log.error("sendSMS>>fail>>templateId not found, userId:{}, type:{}", userId, type);
            return;
        }
        SmsSingleSenderResult result = SMSSend.sendSMS(userBaseDTO.getPhoneCountryCode(), userBaseDTO.getPhone(), templateId);
        if (result.result != 0) {
            log.error("sendSMS>>fail>>phoneCountryCode:{}, phone:{}, type :{}, userId:{}", userBaseDTO.getPhoneCountryCode(), userBaseDTO.getPhone(), type, userId);
        } else {
            log.info("sendSMS>>success>>phoneCountryCode:{}, phone:{}, type :{}, userId:{}", userBaseDTO.getPhoneCountryCode(), userBaseDTO.getPhone(), type, userId);
        }
    }

    public void sendMail(Long userId, Integer type, NotifyDataBase notifyData) throws Exception {
        UserBaseDTO userBaseDTO = userBaseService.getUserBaseInfoByUserId(userId);
        if (StringUtils.isBlank(userBaseDTO.getEmail())) {
            log.info("sendMail>>not binding email, userId:{}, type:{}", userId, type);
            return;
        }
        String email = userBaseDTO.getEmail();
        Integer lang = com.fota.common.enums.LanguageEnum.convertToNotifyLang(userBaseDTO.getLang());
        InformEnum informEnum;
        if (MqConstant.TYPE_MARGIN.equals(type)) {
            informEnum = InformEnum.SUPPLEMENT_MARGIN;
        } else if (MqConstant.TYPE_FORCE.equals(type)) {
            informEnum = InformEnum.FORCE_QUARE;
        } else if (MqConstant.TYPE_PENETRATE.equals(type)){
            informEnum = InformEnum.SUPPLEMENT_BTC;
        } else {
            log.error("sendMail>>fail>>templateId not found, userId:{}, type:{}", userId, type);
            return;
        }
        emailSend.sendMail(email, informEnum, lang, notifyData);
        log.info("sendMail>>success>>email:{}, userId:{}, type:{}", email, userId, type);
    }

    /**
     * 根据用户获取持仓并过滤持仓数量为的持仓
     * @param userId
     * @return
     */
    public List<UserPositionDTO> listPositionByUserId(Long userId, Set<Long> deliveringIdSet) {
        List<UserPositionDTO> userPositionDTOList = userPositionManager.listPositionByUserId(userId);
        if (CollectionUtils.isEmpty(userPositionDTOList)) {
            return userPositionDTOList;
        }

        //过滤交割中合约的持仓和持仓数量为零的持仓
        if (CollectionUtils.isNotEmpty(deliveringIdSet)) {
            Iterator<UserPositionDTO> iterator2 = userPositionDTOList.iterator();
            UserPositionDTO userPositionDTO;
            while (iterator2.hasNext()) {
                userPositionDTO = iterator2.next();
                if (BigDecimal.ZERO.compareTo(userPositionDTO.getAmount()) == 0 || deliveringIdSet.contains(userPositionDTO.getContractId())) {
                    iterator2.remove();
                }
            }
        } else {
            //过滤持仓数量为零的持仓
            Iterator<UserPositionDTO> iterator = userPositionDTOList.iterator();
            while (iterator.hasNext()) {
                if (BigDecimal.ZERO.compareTo(iterator.next().getAmount()) == 0) {
                    iterator.remove();
                }
            }
        }

        return userPositionDTOList;
    }


    /**
     * 获取挂单 过滤交割中合约的挂单
     * @param baseQuery
     * @param deliveringIdSet
     * @return
     */
    public List<ContractOrderDTO> getAllContractOrder(BaseQuery baseQuery, Set<Long> deliveringIdSet) {
        List<ContractOrderDTO> contractOrderDTOList = contractOrderService.getAllContractOrder(baseQuery);
        if (CollectionUtils.isEmpty(contractOrderDTOList)) {
            return contractOrderDTOList;
        }
        if (CollectionUtils.isNotEmpty(deliveringIdSet)) {
            Iterator<ContractOrderDTO> iterator2 = contractOrderDTOList.iterator();
            while (iterator2.hasNext()) {
                if (deliveringIdSet.contains(iterator2.next().getContractId())) {
                    iterator2.remove();
                }
            }
        }
        return contractOrderDTOList;
    }


    /**
     * 获取交割中合约Id
     * @return
     */
    public Set<Long> getDeliveringContractIds() {
        Set<Long> deliveringIdSet = new HashSet<>();
        List<ContractCategoryDTO> deliveringContracts = contractCategoryManager.getContractByStatus(ContractStatus.DELIVERING.getCode());
        if (CollectionUtils.isNotEmpty(deliveringContracts)) {
            for (ContractCategoryDTO contractCategoryDTO : deliveringContracts) {
                deliveringIdSet.add(contractCategoryDTO.getId());
            }
        }
        return deliveringIdSet;
    }

    /**
     * 冻结用户提币
     * @param userId
     * @return
     */
    public void updateOrInsertUserOperationLimit(Long userId) {
        try {
            redisManager.saveValue(CommonConstant.MARGIN_PENETRATE_LIMIT_WITHDRAW_USER + userId, "");
        } catch (Exception e) {
            log.error("updateOrInsertUserOperationLimit exception:", e);
        }
//        ResultCode resultCode = null;
//        try {
//            resultCode = operationLimitService.marginLimitOperation(userId, LimitTypeEnum.WITHDRAW_FREEZE, null, "保证金-穿仓-限制提币", -1L);
//        } catch (Exception e) {
//            log.error("operationLimitService.marginLimitOperation exception:", e);
//            return false;
//        }
//        if (!resultCode.isSuccess()) {
//            log.error("operationLimitService.marginLimitOperation, code:{}, msg:{}", resultCode.getCode(), resultCode.getMessage());
//            return false;
//        }
//        return true;
    }

    /**
     * 解除冻结用户提币
     * @param userId
     * @return
     */
    public void marginUnfreezeLimitOperation(Long userId) {
        try {
            redisManager.deleteValue(CommonConstant.MARGIN_PENETRATE_LIMIT_WITHDRAW_USER + userId);
        } catch (Exception e) {
            log.error("marginUnfreezeLimitOperation exception:", e);
        }
//        ResultCode resultCode = null;
//        try {
//            resultCode = operationLimitService.marginUnfreezeLimitOperation(userId, LimitTypeEnum.WITHDRAW_FREEZE, "保证金-解除限制提币", -1L);
//        } catch (Exception e) {
//            log.error("operationLimitService.marginUnfreezeLimitOperation exception:", e);
//            return false;
//        }
//        if (!resultCode.isSuccess()) {
//            log.error("operationLimitService.marginUnfreezeLimitOperation, code:{}, msg:{}", resultCode.getCode(), resultCode.getMessage());
//            return false;
//        }
//        return true;
    }


    /**
     * 依次获取有效现货实时指数、最新买一卖一价、最新成交价，直到获取到价格为止
     * @param contractCategoryDTOList
     * @return
     */
    public Map<String, BigDecimal> getLatestPriceMap(List<ContractCategoryDTO> contractCategoryDTOList) {
        Map<String, BigDecimal> latestPriceMap = new HashMap<>();
        if (CollectionUtils.isEmpty(contractCategoryDTOList)) {
            return latestPriceMap;
        }
        Map<String, List<Long>> assetContractListMap = contractCategoryDTOList.stream().collect(Collectors.groupingBy(contractCategoryDTO -> contractCategoryDTO.getAssetName().toUpperCase(), Collectors.mapping(ContractCategoryDTO::getId, Collectors.toList())));
        long time = System.currentTimeMillis();
        int valid = 30000;
        List<TickerDTO> tickerDTOList = null;
        try {
            tickerDTOList = indexCacheManager.listCurrentSpotIndex(contractAssetList);
        } catch (Exception e) {
            log.error("getLatestPriceMap>>listTicker Exception!", e);
        }
        if (tickerDTOList == null) {
            tickerDTOList = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(tickerDTOList)) {
            log.error("getLatestPriceMap>>listTicker return empty!");
        } else {
            int size = tickerDTOList.size();
            tickerDTOList = tickerDTOList.stream().filter(tickerDTO -> (tickerDTO.getTime() != null && org.apache.commons.lang3.StringUtils.isNotBlank(tickerDTO.getSymbol()) && tickerDTO.getPrice() != null)).collect(Collectors.toList());
            if (size != tickerDTOList.size()) {
                log.error("getLatestPriceMap>>listTicker return error! tickerDTOList:{}", JSON.toJSONString(tickerDTOList));
            }
            if (CollectionUtils.isEmpty(tickerDTOList)) {
                log.error("getLatestPriceMap>>listTicker after filter empty!");
            }
        }
        if (count != tickerDTOList.size()) {
            this.initInvalidIndexAssetMap(time, tickerDTOList);
        }

        for (TickerDTO tickerDTO : tickerDTOList) {
            if ((tickerDTO.getTime() >= time) || time - tickerDTO.getTime() <= valid) {
                if (invalidIndexAssetMap.containsKey(tickerDTO.getSymbol().toUpperCase())) {
                    if (switchToSpotIndex(tickerDTO)) {
                        latestPriceMap.putAll(getLatestPriceMapByAsset(tickerDTO.getSymbol().toUpperCase(), tickerDTO.getPrice(), assetContractListMap));
                        invalidIndexAssetMap.remove(tickerDTO.getSymbol().toUpperCase());
                        log.info("getLatestPriceMap>>switchToSpotIndex, tickerDTO:{}", tickerDTO);
                    }
                } else {
                    latestPriceMap.putAll(getLatestPriceMapByAsset(tickerDTO.getSymbol().toUpperCase(), tickerDTO.getPrice(), assetContractListMap));
                }
            } else {
                invalidIndexAssetMap.put(tickerDTO.getSymbol().toUpperCase(), tickerDTO.getTime());
                log.info("getLatestPriceMap>>switchToRealTimeEntrust, tickerDTO:{}", tickerDTO);
            }
        }

        if (!invalidIndexAssetMap.isEmpty()) {
            Map<String, BigDecimal> latestDealPriceMap = getRealTimeEntrust();
            List<Long> contractIdList;
            for (Map.Entry<String, Long> entry : invalidIndexAssetMap.entrySet()) {
                contractIdList = assetContractListMap.get(entry.getKey());
                if (CollectionUtils.isEmpty(contractIdList)) {
                    log.warn("getLatestPriceMap>>contractIdList is null! assetName:{}, assetContractListMap:{}", entry.getKey(), JSON.toJSONString(assetContractListMap));
                    continue;
                }
                for (Long contractId : contractIdList) {
                    latestPriceMap.put(contractId + PriceDirectionEnum.SELL.getName(), latestDealPriceMap.get(contractId + PriceDirectionEnum.SELL.getName()));
                    latestPriceMap.put(contractId + PriceDirectionEnum.BUY.getName(), latestDealPriceMap.get(contractId + PriceDirectionEnum.BUY.getName()));
                }
            }
        }

        return latestPriceMap;
    }

    private void initInvalidIndexAssetMap(long current, List<TickerDTO> tickerDTOList) {
        List<String> contractSymbols = Arrays.asList(CoinSymbolEnum.CONTRACT_SYMBOL.getName().split(","));
        List<String> assetNameList = tickerDTOList.stream().map(tickerDTO -> tickerDTO.getSymbol().toUpperCase()).collect(Collectors.toList());
        contractSymbols = contractSymbols.stream().filter(symbol -> !assetNameList.contains(symbol)).collect(Collectors.toList());
        long middleTime = TimeUtils.getMiddleMillis(current);
        for (String assetName : contractSymbols) {
            if (!invalidIndexAssetMap.containsKey(assetName)) {
                invalidIndexAssetMap.put(assetName, middleTime);
                log.info("getLatestPriceMap>>switchToRealTimeEntrust, assetName:{}, time:{}", assetName, middleTime);
            }
        }
    }

    /**
     * 获取标的物所有合约的价格
     * @param assetName
     * @param price
     * @param assetContractListMap
     * @return
     */
    private Map<String, BigDecimal> getLatestPriceMapByAsset(String assetName, BigDecimal price, Map<String, List<Long>> assetContractListMap) {
        Map<String, BigDecimal> latestPriceMap = new HashMap<>();
        List<Long> contractIdList = assetContractListMap.get(assetName);
        if (CollectionUtils.isEmpty(contractIdList)) {
            log.warn("getLatestPriceMap>>getLatestPriceMapByAsset contractIdList is null! assetName:{}, assetContractListMap:{}", assetName, JSON.toJSONString(assetContractListMap));
            return latestPriceMap;
        }
        for (Long contractId : contractIdList) {
            latestPriceMap.put(contractId + PriceDirectionEnum.SELL.getName(), price);
            latestPriceMap.put(contractId + PriceDirectionEnum.BUY.getName(), price);
        }
        return latestPriceMap;
    }

    /**
     * 判断是否切换到现货指数
     * @return
     */
    private boolean switchToSpotIndex(TickerDTO tickerDTO) {
        Long invalidStart = invalidIndexAssetMap.get(tickerDTO.getSymbol().toUpperCase());
        if (invalidStart == null) {
            return false;
        }
        Long previous = tickerDTO.getTime() - 10000;
        if (previous >= invalidStart) {
            List<TickerDTO> tickerDTOList;
            try {
                tickerDTOList = deliveryIndexService.listRecentTicker(null, previous, tickerDTO.getTime());
            } catch (Exception e) {
                log.error("getLatestPriceMap>>listRecentTicker Exception!", e);
                return false;
            }
            if (CollectionUtils.isEmpty(tickerDTOList)) {
                log.error("getLatestPriceMap>>listRecentTicker is empty!");
                return false;
            }
            String assetName = tickerDTO.getSymbol().toUpperCase();
            tickerDTOList = tickerDTOList.stream().filter(ticker -> assetName.equals(ticker.getSymbol().toUpperCase())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tickerDTOList) && tickerDTOList.size() == 21) {
                return true;
            } else {
                ForcedLog.forcedLog("getLatestPriceMap>> tickerDTOList:{}", JSON.toJSONString(tickerDTOList));
            }
        }
        return false;
    }
    /**
     * 获取最新卖一买一价或最新成交价
     * @return
     */
    public Map<String, BigDecimal> getRealTimeEntrust() {
        Map<String, BigDecimal> latestPriceMap = new HashMap<>();
        List<CompetitorsPriceDTO> competitorsPriceDTOList = new ArrayList<>();
        try {
            competitorsPriceDTOList = realTimeEntrust.getContractCompetitorsPrice();
        } catch (Exception e) {
            log.error("getLatestPriceMap>>getContractCompetitorsPrice exception!", e);
        }
        if (CollectionUtils.isEmpty(competitorsPriceDTOList)) {
            log.error("getLatestPriceMap>>getContractCompetitorsPrice return empty!");
        }
        for (CompetitorsPriceDTO competitorsPriceDTO : competitorsPriceDTOList) {
            if (com.fota.ticker.entrust.entity.enums.OrderDirectionEnum.SELL.getCode() == competitorsPriceDTO.getOrderDirection()) {
                latestPriceMap.put(competitorsPriceDTO.getId() + PriceDirectionEnum.SELL.getName(), competitorsPriceDTO.getPrice());
            } else if (com.fota.ticker.entrust.entity.enums.OrderDirectionEnum.BUY.getCode() == competitorsPriceDTO.getOrderDirection()) {
                latestPriceMap.put(competitorsPriceDTO.getId() + PriceDirectionEnum.BUY.getName(), competitorsPriceDTO.getPrice());
            } else {
                log.error("getLatestPriceMap>>competitorsPriceDTO orderDirection unknown，competitorsPriceDTO:{}", JSON.toJSONString(competitorsPriceDTO));
            }
        }
        return latestPriceMap;
    }

    /**
     * 获取交割指数
     * @return
     */
    public Map<String, BigDecimal> getDeliveryIndex(Long deliveryTime) {
        Map<String, BigDecimal> deliveryIndexList = new HashMap<>();
        long time = System.currentTimeMillis();
        List<TickerDTO> tickerDTOList = null;
        try {
            if (deliveryTime == null) {
                tickerDTOList = indexCacheManager.listCurrentSpotIndex(contractAssetList);
            } else {
                tickerDTOList = spotIndexService.listHistoryTicker(CoinSymbolEnum.CONTRACT_SYMBOL.getName(), TimeUtils.getMiddleMillis(time));
            }
        } catch (Exception e) {
            log.error("Delivery>>getDeliveryIndex>>listTicker Exception!", e);
            return deliveryIndexList;
        }
        if (CollectionUtils.isEmpty(tickerDTOList)) {
            return deliveryIndexList;
        }
        log.info("Delivery>>getDeliveryIndex>>tickerDTOList:{}", JSON.toJSONString(tickerDTOList));
        tickerDTOList = tickerDTOList.stream().filter(tickerDTO -> (tickerDTO.getTime() != null && org.apache.commons.lang3.StringUtils.isNotBlank(tickerDTO.getSymbol()) && tickerDTO.getPrice() != null)).collect(Collectors.toList());
        log.info("Delivery>>getDeliveryIndex>>tickerDTOList after filter null:{}", JSON.toJSONString(tickerDTOList));
        if (CollectionUtils.isEmpty(tickerDTOList)) {
            log.error("Delivery>>getDeliveryIndex>>listTicker return null param!");
            return deliveryIndexList;
        }
        int valid = 30000;
        tickerDTOList = tickerDTOList.stream().filter(tickerDTO -> ((tickerDTO.getTime() >= time) || time - tickerDTO.getTime() <= valid)).collect(Collectors.toList());
        log.info("Delivery>>getDeliveryIndex>>tickerDTOList after filter invalid:{}", JSON.toJSONString(tickerDTOList));
        if (CollectionUtils.isEmpty(tickerDTOList)) {
            log.error("Delivery>>getDeliveryIndex>>listTicker return invalid!");
            return deliveryIndexList;
        }
        deliveryIndexList.putAll(tickerDTOList.stream().collect(Collectors.toMap(ticker -> ticker.getSymbol().toUpperCase(), TickerDTO::getPrice)));
        return deliveryIndexList;
    }
}
