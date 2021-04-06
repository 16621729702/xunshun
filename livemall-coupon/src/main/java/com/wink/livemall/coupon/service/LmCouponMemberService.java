package com.wink.livemall.coupon.service;

import com.wink.livemall.coupon.dto.LmConpouMember;

import java.util.List;

public interface LmCouponMemberService {

    LmConpouMember findById(Integer id);

    int updateService(LmConpouMember lmConpouMember);

    List<LmConpouMember> findByMemberId(Integer memberId,Integer couponId);

    int insert(LmConpouMember lmConpouMember);

    LmConpouMember findByOrderId(Integer orderId);

}
