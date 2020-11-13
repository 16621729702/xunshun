package com.wink.livemall.sys.consult.dao;

import com.wink.livemall.sys.consult.dto.Consult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ConsultDao extends tk.mybatis.mapper.common.Mapper<Consult>{

    @Select("select title from lm_sys_consult where status = 0")
    List<String> findActiveList();

    @Select("select content from lm_sys_consult where title = #{title} limit 0,1")
    String findInfoByTitle(@Param("title") String title);
}
