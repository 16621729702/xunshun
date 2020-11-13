package com.wink.livemall.sys.basic.dao;

import com.wink.livemall.sys.basic.dto.LmBasicConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LmBasicConfigDao extends tk.mybatis.mapper.common.Mapper<LmBasicConfig>{

    @Select("SELECT * FROM lm_basic_config WHERE type = #{type} AND status = 'active'")
    LmBasicConfig findByType(@Param("type")String type);
}
