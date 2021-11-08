package com.fota.fotamargin.dao.trade.mapper;

import com.fota.trade.client.domain.ContractCategoryDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContractCategoryMapper {

    @Select({
            "select",
            "id, gmt_create, gmt_modified, contract_name, asset_id, asset_name, total_amount, ",
            "unfilled_amount, delivery_date, status, contract_type, price",
            "from trade_contract_category",
            "where status = #{status,jdbcType=TINYINT}"
    })
    @ResultMap("BaseResultMap")
    List<ContractCategoryDO> selectByStatus(Integer status);
}