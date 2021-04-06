package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.AgencyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AgencyInfoDao extends tk.mybatis.mapper.common.Mapper<AgencyInfo>{


    /**
     * 查询
     * @param userId
     * @return
     */
    @Select("SELECT * FROM agency_info WHERE user_id = #{userId} and state = 2   limit 1 ")
    AgencyInfo findListByUserId(@Param("userId")int userId);


}
