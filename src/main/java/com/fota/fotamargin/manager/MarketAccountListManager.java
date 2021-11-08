package com.fota.fotamargin.manager;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author taoyuanming
 * Created on 2018/11/29
 * Description
 */
@Slf4j
@Service
public class MarketAccountListManager {

    private List<Long> marketAccountList;

    public boolean contains(Long userId) {
        return Objects.nonNull(marketAccountList) && marketAccountList.contains(userId);
    }

    @Value("${trade.marketAccountList}")
    public void setMarketAccountList(String[] marketAccountArray) {
        if (Objects.nonNull(marketAccountArray)) {
            marketAccountList = new ArrayList<>(marketAccountArray.length);
            for (String marketAccount : marketAccountArray) {
                marketAccountList.add(NumberUtils.toLong(marketAccount));
            }
        }
    }
}
