package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.LmMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmMemberDao extends tk.mybatis.mapper.common.Mapper<LmMember> {

    @SelectProvider(type = LmMemberDaoprovider.class, method = "findListByCondient")
    List<LmMember> findByCondient(Map<String, String> condient);

    @Select("SELECT * FROM lm_member WHERE mobile = #{mobile}")
    LmMember findByMobile(@Param("mobile")String mobile);

    @Select("SELECT * FROM lm_member WHERE id = #{id}")
    Map<String ,Object> findByIdList(@Param("id")int id);

    class LmMemberDaoprovider{

        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT * from lm_member   ";
            if(!StringUtils.isEmpty(condient.get("realname"))){
                sql += " where realname like '%"+condient.get("realname")+"%'";
            }
            sql +=" order by id desc ";
            return sql;
        }

    }
}
