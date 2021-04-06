package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.LmMemberCoupon;
import com.wink.livemall.member.dto.LmMemberFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmMemberCouponDao  extends tk.mybatis.mapper.common.Mapper<LmMemberCoupon> {


    @Select("SELECT * FROM lm_member_coupon WHERE member_id = #{memberid}")
    List<LmMemberCoupon> findByMemberid(@Param("memberid")int memberid);


}
