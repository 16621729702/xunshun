package com.wink.livemall.merch.dao;

import com.wink.livemall.merch.dto.LmMerchAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmMerchAdminDao extends tk.mybatis.mapper.common.Mapper<LmMerchAdmin>{


    @Select("SELECT * FROM lm_merch_admin where memberid = #{memberid} and merchid = #{merchid} limit 0,1 ")
    LmMerchAdmin findMerchAdmin(@Param("memberid") int memberid, @Param("merchid")int merchid);


    @Select("SELECT lma.* ,lm.mobile as mobile FROM lm_merch_admin lma,lm_member lm where lma.memberid = lm.id and lma.merchid = #{merchid}  ")
    List<Map<String, Object>> findAdminInfo(@Param("merchid")int merchid);

    @Select("SELECT * FROM lm_merch_admin where memberid = #{memberid} ")
    List<LmMerchAdmin> findAdminByMember(int id);
}
