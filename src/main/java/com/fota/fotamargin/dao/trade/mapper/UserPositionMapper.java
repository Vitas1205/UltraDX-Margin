package com.fota.fotamargin.dao.trade.mapper;

import com.fota.trade.client.domain.UserPositionDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserPositionMapper {


    @Select({
            "select",
            "id, gmt_create, gmt_modified, user_id, contract_id, contract_name, locked_amount, ",
            "unfilled_amount, position_type, average_price,status,lever, fee_rate",
            "from trade_user_position",
            "where  contract_id = #{contractId,jdbcType=BIGINT} and status = #{status} and unfilled_amount > 0"
    })
    @ResultMap("BaseResultMap")
    List<UserPositionDO> selectByContractId(@Param("contractId") Long contractId, @Param("status") Integer status);

    List<UserPositionDO> selectByUserId(@Param("userId") Long userId, @Param("status") Integer status);

}