package com.fota.fotamargin.dao.mapper;


import com.fota.asset.domain.dao.UserContractDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserContractDOMapper {
    List<UserContractDO> pageContractAccountBySharding(Map<String, Object> map);

    List<Long> pageUserIdBySharding(Map<String, Object> map);

    List<UserContractDO> selectContractAccountByUserId(@Param("userIdList") List<Long> userIdList);

    int updateStatusByUserId(@Param("userId")Long userId, @Param("status")Integer status);
}