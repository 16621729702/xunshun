package com.wink.livemall.order.dao;

import com.wink.livemall.order.dto.LmOrderLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LmShopOrderLogDao extends tk.mybatis.mapper.common.Mapper<LmOrderLog>{

    @Select("SELECT * FROM lm_orders_log WHERE orderid = #{orderid}")
    List<LmOrderLog> findByOrderid(@Param("orderid")String orderid);
}
