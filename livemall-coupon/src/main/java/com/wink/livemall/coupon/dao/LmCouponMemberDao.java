package com.wink.livemall.coupon.dao;


import com.wink.livemall.coupon.dto.LmConpouMember;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LmCouponMemberDao extends tk.mybatis.mapper.common.Mapper<LmConpouMember>{

    @Select("SELECT can_use as canUse FROM lm_coupon_member WHERE member_id = #{memberId} and coupon_id=#{couponId} ORDER BY can_use ")
    List<LmConpouMember> findByMemberId(@Param("memberId") Integer memberId,@Param("couponId") Integer couponId);

    @Select("SELECT id  FROM lm_coupon_member WHERE order_id = #{orderId} and  can_use =0")
    LmConpouMember findByOrderId(@Param("orderId") Integer orderId);
}
