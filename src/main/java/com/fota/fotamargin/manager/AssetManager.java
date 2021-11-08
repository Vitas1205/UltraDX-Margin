package com.fota.fotamargin.manager;

import com.alibaba.fastjson.JSON;
import com.fota.asset.domain.UserContractDTO;
import com.fota.asset.domain.dao.UserContractDO;
import com.fota.asset.domain.enums.UserContractStatus;
import com.fota.common.Page;
import com.fota.common.Result;
import com.fota.fotamargin.dao.mapper.UserContractDOMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yuanming Tao
 * Created on 2018/11/6
 * Description
 */
@Service
public class AssetManager {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final int pageSize = 5000;

    @Autowired
    private UserContractDOMapper userContractDOMapper;

    public List<Long> getUserIdBySharding(Integer shardingTotalCount, Integer shardingItem) {
        List<Long> userIdList = new ArrayList<>();
        Integer pageNum = 1;
        Result<Page<Long>> result;
        try {
            for (; ; pageNum++) {
                result = pageUserIdBySharding(shardingTotalCount, shardingItem, pageNum, pageSize);
                if (!result.isSuccess()) {
                    break;
                }
                userIdList.addAll(result.getData().getData());
                if (result.getData().getData().size() < pageSize) {
                    break;
                }
                if (pageNum > 20000) {
                    log.error("user count out of bounds");
                    break;
                }
            }
        } catch (Exception e) {
            log.error("getUserIdBySharding Exception:", e);
        }
        return userIdList;
    }


    /**
     * 分页获取分片合约账户 按userId排序  (userId % shardingTotalCount = shardingItem)
     *
     * @param shardingTotalCount 分片总数
     * @param shardingItem       分配于本作业实例的分片项
     * @param pageNum            页码
     * @param pageSize           每页记录数
     * @return
     */
    public Result<Page<Long>> pageUserIdBySharding(Integer shardingTotalCount, Integer shardingItem, Integer pageNum, Integer pageSize) {
        Result<Page<Long>> ret = new Result<>();
        Page<Long> page = new Page<>();
        Map<String, Object> param = new HashMap<>();
        param.put("shardingTotalCount", shardingTotalCount);
        param.put("shardingItem", shardingItem);
        param.put("offset", (pageNum - 1) * pageSize);
        param.put("rows", pageSize);
        List<Long> userIdList;
        try {
            userIdList = userContractDOMapper.pageUserIdBySharding(param);
        }catch (Exception e){
            log.error("userContractMapper.pageUserIdBySharding failed! param:{}", String.valueOf(param));
            log.error("userContractMapper.pageUserIdBySharding failed!", e);
            return ret.error(-1, "failed");
        }
        page.setData(userIdList);
        ret.success(page);
        return ret;
    }

    public List<UserContractDTO> selectContractAccountByUserId(List<Long> userIdList) {
        List<UserContractDTO> userContractDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(userIdList)) {
            log.error("selectContractAccountByUserId param userIdList is empty!");
            return userContractDTOList;
        }
        try {
            int size = userIdList.size();
            List<UserContractDO> userContractDOList;
            if (size <= pageSize) {
                userContractDOList = userContractDOMapper.selectContractAccountByUserId(userIdList);
                for (UserContractDO userContractDO : userContractDOList) {
                    userContractDTOList.add(copy(userContractDO));
                }
            } else {
                int i = size / pageSize;
                int from;
                int to;
                for (int k = 0; k < i; k++) {
                    from = pageSize * k;
                    to = pageSize * (k + 1);
                    userContractDOList = userContractDOMapper.selectContractAccountByUserId(userIdList.subList(from, to));
                    for (UserContractDO userContractDO : userContractDOList) {
                        userContractDTOList.add(copy(userContractDO));
                    }
                }
                int j = size % pageSize;
                if (j > 0) {
                    from = pageSize * i;
                    to = from + j;
                    userContractDOList = userContractDOMapper.selectContractAccountByUserId(userIdList.subList(from, to));
                    for (UserContractDO userContractDO : userContractDOList) {
                        userContractDTOList.add(copy(userContractDO));
                    }
                }

            }
        } catch (Exception e) {
            userContractDTOList = new ArrayList<>();
            log.error("userContractMapper.selectContractAccountByUserId failed! param:{}", userIdList);
            log.error("userContractMapper.selectContractAccountByUserId failed!", e);
        }
        return userContractDTOList;
    }
    /**
     * 分页获取分片合约账户 按userId排序  (userId % shardingTotalCount = shardingItem)
     *
     * @param shardingTotalCount 分片总数
     * @param shardingItem       分配于本作业实例的分片项
     * @param pageNum            页码
     * @param pageSize           每页记录数
     * @return
     */
    public Result<Page<UserContractDTO>> pageContractAccountBySharding(Integer shardingTotalCount, Integer shardingItem, Integer pageNum, Integer pageSize) {
        Result<Page<UserContractDTO>> ret = new Result<>();
        Page<UserContractDTO> page = new Page<>();
        Map<String, Object> param = new HashMap<>();
        param.put("shardingTotalCount", shardingTotalCount);
        param.put("shardingItem", shardingItem);
        param.put("offset", (pageNum - 1) * pageSize);
        param.put("rows", pageSize);
        List<UserContractDTO> listDTO = new ArrayList<>();
        try {
            List<UserContractDO> listDO = userContractDOMapper.pageContractAccountBySharding(param);
            for (UserContractDO userContractDO : listDO){
                listDTO.add(copy(userContractDO));
            }
        }catch (Exception e){
            log.error("userContractMapper.pageContractAccountBySharding failed! param:{}", String.valueOf(param));
            log.error("userContractMapper.pageContractAccountBySharding failed!", e);
            return ret.error(-1, "failed");
        }
        page.setData(listDTO);
        ret.success(page);
        return ret;
    }

    public boolean updateContractAccountStatus(long userId, UserContractStatus userContractStatus) {
        boolean  result = false;
        try {
            int ret = userContractDOMapper.updateStatusByUserId(userId, userContractStatus.getCode());
            if (ret == 1){
                result = true;
            }
        }catch (Exception e){
            log.error("userContractMapper.updateStatusByUserId failed{}",userId,e);
        }

        return result;
    }

    public static UserContractDTO copy(UserContractDO userContractDO) {
        UserContractDTO userContractDTO = new UserContractDTO();
        userContractDTO.setId(userContractDO.getId());
        userContractDTO.setUserId(userContractDO.getUserId());
        userContractDTO.setAmount(userContractDO.getAmount().setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
        userContractDTO.setLockedAmount(userContractDO.getLockedAmount().setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
        userContractDTO.setGmtCreate(userContractDO.getGmtCreate());
        userContractDTO.setGmtModified(userContractDO.getGmtModified());
        userContractDTO.setVersion(userContractDO.getVersion());
        userContractDTO.setStatus(userContractDO.getStatus());
        return userContractDTO;
    }
}
