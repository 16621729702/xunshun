package com.wink.livemall.help.dao;

import com.wink.livemall.help.dto.LmHelpCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
@Mapper
public interface LmHelpCategoryDao extends tk.mybatis.mapper.common.Mapper<LmHelpCategory>{

    @Select("SELECT id as id , name as name FROM lm_help_category WHERE pid = 0 and state = 0 and isshow = 0 order by sort desc")
    List<Map<String, String>> findTopByApi();

    @Select("SELECT id as id , name as name FROM lm_help_category WHERE pid = #{pid} and state = 0 and isshow = 0 order by sort desc")
    List<Map<String, String>> findByPidByApi(@Param("pid")int pid);
}
