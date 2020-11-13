package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.LmMemberAddress;
import com.wink.livemall.member.dto.LmMemberCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmMemberAddressDao extends tk.mybatis.mapper.common.Mapper<LmMemberAddress> {

    @Select("SELECT  * FROM lm_member_address WHERE member_id = #{memberid} order by is_default desc ")
    List<LmMemberAddress> findByMemberid(@Param("memberid")int memberid);

    @Select("SELECT id as id,mobile as mobile,realname as realname,address_info as address_info,province as province, city as city ,is_default as is_default ,district as district FROM lm_member_address WHERE member_id = #{memberid} order by is_default desc ")
    List<Map<String, String>> findByMemberidByapi(@Param("memberid")int memberid);
}
