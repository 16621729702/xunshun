package com.wink.livemall.sys.setting.dao;


import com.wink.livemall.sys.setting.dto.VersionIOS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VersionIOSDao extends tk.mybatis.mapper.common.Mapper<VersionIOS>{

    @Select("SELECT * FROM lm_version_ios WHERE status = 1 ")
    VersionIOS findActiveIOS();

}
