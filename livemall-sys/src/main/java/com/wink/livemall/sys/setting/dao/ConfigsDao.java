package com.wink.livemall.sys.setting.dao;

import com.wink.livemall.sys.setting.dto.Configs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ConfigsDao extends  tk.mybatis.mapper.common.Mapper<Configs>{

    @Select("SELECT * FROM lm_setting_configs WHERE type = #{type}")
    Configs findByTypeId(@Param("type")int typeid);
}
