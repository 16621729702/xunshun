package com.wink.livemall.merch.dao;

import com.wink.livemall.merch.dto.LmMerchConfigs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LmMerchConfigsDao extends tk.mybatis.mapper.common.Mapper<LmMerchConfigs>{

    @Select("SELECT * FROM lm_merch_configs WHERE type = #{type}")
    LmMerchConfigs findByType(@Param("type")String type);
}
