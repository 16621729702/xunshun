package com.wink.livemall.live.dao;

import com.wink.livemall.live.dto.LmLiveInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LmLiveInfoDao extends tk.mybatis.mapper.common.Mapper<LmLiveInfo> {

    @Select("SELECT * FROM lm_live_info WHERE live_id =#{liveid}")
    LmLiveInfo findLiveInfo(@Param("liveid")int liveid);
}
