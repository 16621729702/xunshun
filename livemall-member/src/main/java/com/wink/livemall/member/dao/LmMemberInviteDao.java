package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.LmMemberInvite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LmMemberInviteDao extends tk.mybatis.mapper.common.Mapper<LmMemberInvite> {


    @Select("SELECT * FROM lm_member_invite WHERE invite_code = #{inviteCode}")
    LmMemberInvite findByMemberId(@Param("inviteCode") String inviteCode);

    @Select("SELECT * FROM lm_member_invite WHERE member_id = #{memberId}")
    LmMemberInvite findByInviteCode(@Param("memberId") int memberId);

}
