package com.wink.livemall.sys.setting.dao;

import com.wink.livemall.sys.setting.dto.Version;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VersionDao extends tk.mybatis.mapper.common.Mapper<Version>{

    @Select("SELECT * FROM lm_version WHERE status = 1")
    Version findActive();
}
