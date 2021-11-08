package com.fota.fotamargin.dao.trade.mapper;

import com.fota.trade.client.domain.UserContractLeverDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Gavin Shen
 * @Date 2018/7/12
 */
@Mapper
public interface UserContractLeverMapper {


    @Select({
            "select ",
            "id, gmt_create, gmt_modified, user_id, asset_id, asset_name, lever",
            "from trade_user_lever",
            "where  user_id = #{userId,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    List<UserContractLeverDO> listUserContractLever(Long userId);

}
