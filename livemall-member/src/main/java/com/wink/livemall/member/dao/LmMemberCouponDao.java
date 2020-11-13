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

    @Select("SELECT lm.store_name as merchname ,lmc.price as pirce , lmc.comment as comment, lmc.start_time as starttime, lmc.end_time as endtime FROM lm_member_coupon lmc left join lm_merch_info lm on lmc.merch_id = lm.id WHERE lmc.member_id = #{memberid} ")
    List<Map<String, String>> findByMemberidByApi(@Param("memberid")int memberid);
}
