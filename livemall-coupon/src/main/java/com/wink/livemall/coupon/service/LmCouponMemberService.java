package com.wink.livemall.coupon.service;

import com.wink.livemall.coupon.dto.LmConpouMember;

public interface LmCouponMemberService {

    LmConpouMember findById(Integer id);

    int insert(LmConpouMember lmConpouMember);

}
