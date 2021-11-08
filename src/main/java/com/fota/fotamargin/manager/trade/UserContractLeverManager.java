package com.fota.fotamargin.manager.trade;

import com.fota.asset.domain.enums.AssetTypeEnum;
import com.fota.fotamargin.dao.trade.mapper.UserContractLeverMapper;
import com.fota.trade.client.domain.UserContractLeverDO;
import com.fota.trade.domain.UserContractLeverDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fota.fotamargin.common.constant.Constant.DEFAULT_LEVER;

/**
 * @author Yuanming Tao
 * Created on 2018/11/16
 * Description
 */
@Slf4j
@Service
public class UserContractLeverManager {

    @Autowired
    private UserContractLeverMapper userContractLeverMapper;

    public List<UserContractLeverDTO> listUserContractLever(long userId) {
        List<UserContractLeverDTO> resultList = new ArrayList<>();
        List<UserContractLeverDO> list;

        try {
            list = userContractLeverMapper.listUserContractLever(userId);
            if (CollectionUtils.isEmpty(list)) {
                list = new LinkedList<>();
            }
            Set<Integer> leverAssetList = list.stream().map(UserContractLeverDO::getAssetId).collect(Collectors.toSet());
            List<UserContractLeverDO> defaultLevers = Stream.of(AssetTypeEnum.values()).filter(x -> x.isValid() && !leverAssetList.contains(x.getCode()))
                    .map(x -> {
                        UserContractLeverDO userContractLeverDO = new UserContractLeverDO();
                        userContractLeverDO.setAssetId(x.getCode());
                        userContractLeverDO.setAssetName(x.name());
                        userContractLeverDO.setLever(DEFAULT_LEVER);
                        return userContractLeverDO;
                    })
                    .collect(Collectors.toList());

            list.addAll(defaultLevers);

            List<Integer> validAssetCodes = AssetTypeEnum.getValidAssetCodes();
            if (list != null && list.size() > 0) {
                for (UserContractLeverDO userContractLeverDO : list) {
                    if (!validAssetCodes.contains(userContractLeverDO.getAssetId())) {
                        continue;
                    }
                    UserContractLeverDTO newUserContractLeverDTO = new UserContractLeverDTO();
                    newUserContractLeverDTO.setAssetId(userContractLeverDO.getAssetId());
                    newUserContractLeverDTO.setAssetName(userContractLeverDO.getAssetName());
                    newUserContractLeverDTO.setLever(userContractLeverDO.getLever());
                    resultList.add(newUserContractLeverDTO);
                }
            }
        } catch (Exception e) {
            log.error("userContractLeverMapper.listUserContractLever({})", userId,  e);
        }
        return resultList;
    }

}
