package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.LmMemberLevel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmMemberLevelDao extends tk.mybatis.mapper.common.Mapper<LmMemberLevel>{

    @SelectProvider(type = LmMemberLevelDaoprovider.class, method = "findListByCondient")
    List<LmMemberLevel> findByCondient(Map<String, String> condient);

    @Select("SELECT  growth_value as growth_value ,name as name FROM lm_member_level WHERE state = 0 order by growth_value")
    List<Map<String, String>> findInfoByApi();

    class LmMemberLevelDaoprovider{

        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT * from lm_member_level where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("name"))){
                sql += " and name like '%"+condient.get("name")+"%'";
            }
            return sql;
        }

    }
}
