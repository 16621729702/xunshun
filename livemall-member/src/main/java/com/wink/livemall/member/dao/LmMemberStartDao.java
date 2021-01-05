package com.wink.livemall.member.dao;


import com.wink.livemall.member.dto.LmMemberStart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LmMemberStartDao extends tk.mybatis.mapper.common.Mapper<LmMemberStart>{

    @Select("SELECT * FROM lm_member_start WHERE mobile = #{mobile} and type = #{type} and isstart = 1")
    List<LmMemberStart>  findByMobile(@Param("mobile")String mobile,@Param("type")int type);


}
