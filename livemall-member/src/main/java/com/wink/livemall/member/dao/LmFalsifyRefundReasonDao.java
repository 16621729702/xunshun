package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.LmFalsifyRefundReason;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LmFalsifyRefundReasonDao extends tk.mybatis.mapper.common.Mapper<LmFalsifyRefundReason> {


    @Select("SELECT * FROM lm_falsify_refund_reason where member_id= #{memberId} and good_id =#{goodId} and type =#{type}")
    LmFalsifyRefundReason findRefundReason(@Param("memberId")int memberId,@Param("goodId")int goodId,@Param("type")int type);

}
