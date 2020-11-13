package com.wink.livemall.order.dao;

import com.wink.livemall.order.dto.LmPayLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LmPayLogDao extends tk.mybatis.mapper.common.Mapper<LmPayLog>{

    @Select("SELECT * FROM lm_paylog WHERE type = #{type} and orderno = #{merOrderId} order by createtime desc limit 0,1")
    LmPayLog findBymerOrderIdAndType(@Param("merOrderId")String merOrderId, @Param("type")String type);


}
