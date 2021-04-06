package com.wink.livemall.live.dao;

import com.wink.livemall.live.dto.LmLiveRenew;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmLiveRenewDao extends tk.mybatis.mapper.common.Mapper<LmLiveRenew>{


    @Select("SELECT *  FROM lm_live_renew  where  live_pay_sn = #{livePaySn} order by create_time desc limit 1 ")
    LmLiveRenew findLmLiveRenew(@Param("livePaySn")String livePaySn);


}
