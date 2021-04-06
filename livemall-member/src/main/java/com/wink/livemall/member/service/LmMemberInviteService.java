package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.LmMemberInvite;
import org.apache.ibatis.annotations.Param;

public interface LmMemberInviteService {

    LmMemberInvite getInviteCode(String memberId);

    LmMemberInvite findByInviteCode( String memberId);

    void addShareCoupon(String memberId, LmMemberInvite lmMemberInvite);
}
