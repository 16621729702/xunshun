package com.wink.livemall.merch.dao;

import com.wink.livemall.merch.dto.LmMerchCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmMerchCategoryDao extends tk.mybatis.mapper.common.Mapper<LmMerchCategory>{

    @Select("SELECT * FROM lm_merch_category WHERE isshow = 0")
    List<LmMerchCategory> findActiceListByApi();
}
