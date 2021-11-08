package com.fota.fotamargin.dao.trade.mapper;

import com.fota.trade.client.domain.ContractOrderDO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContractOrderMapper {

    int countByQuery(Map<String, Object> param);

    List<ContractOrderDO> listByQuery(Map<String, Object> param);

}