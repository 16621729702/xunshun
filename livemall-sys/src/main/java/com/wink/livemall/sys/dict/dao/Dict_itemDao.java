package com.wink.livemall.sys.dict.dao;

import com.wink.livemall.sys.dict.dto.LmSysDictItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Dict_itemDao extends   tk.mybatis.mapper.common.Mapper<LmSysDictItem>{

    @Select("SELECT * FROM lm_sys_dict_item WHERE dict_code = #{code}")
    List<LmSysDictItem> findByDictCode(@Param("code")String code);
}
