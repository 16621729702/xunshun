package com.wink.livemall.merch.dao;

import com.wink.livemall.merch.dto.LmWithdrawal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmWithdrawalDao extends tk.mybatis.mapper.common.Mapper<LmWithdrawal>{

    @Select("SELECT " +
            " lm.store_name as store_name, " +
            " lm.realname as realname, " +
            " lm.credit as credit, " +
            " lw.* " +
            " FROM lm_withdrawal lw,lm_merch_info lm where lw.merchid = lm.id order by status ")
    List<Map<String, Object>> findInfoList();

    @Select("SELECT " +
            " lm.store_name as store_name, " +
            " lm.realname as realname, " +
            " lm.credit as credit, " +
            " lw.* " +
            " FROM lm_withdrawal lw,lm_merch_info lm where lw.merchid = lm.id and lw.id = #{id} ")
    Map<String, Object> findinfoById(@Param("id")int id);
}
