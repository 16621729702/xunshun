package com.wink.livemall.help.dao;

import com.wink.livemall.help.dto.LmHelpInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmHelpInfoDao extends tk.mybatis.mapper.common.Mapper<LmHelpInfo> {

    @Select("SELECT id as id , title as name FROM lm_help_info WHERE h_category_id = #{categoryid}  order by sort desc")
    List<Map<String, String>> findBycategoryid(@Param("categoryid") String categoryid);
}
