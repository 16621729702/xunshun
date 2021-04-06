package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.LmMemberLink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LmMemberLinkDao extends tk.mybatis.mapper.common.Mapper<LmMemberLink>{


    @Select("SELECT * FROM lm_member_link where member_id = #{memberId}  ")
    List<LmMemberLink>  findMember(@Param("memberId")int memberId);

    @Select("SELECT * FROM lm_member_link where  link_id = #{memberId} ")
    List<LmMemberLink>  findLinkId(@Param("memberId")int memberId);


    @Select("SELECT * FROM lm_member_link where member_id = #{memberId} or link_id = #{linkId} ")
    List<LmMemberLink>  findLink(@Param("memberId")int memberId,@Param("linkId")int linkId);


}
