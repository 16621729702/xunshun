package com.wink.livemall.goods.dao;

import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.GoodSpec;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodSpecDao extends tk.mybatis.mapper.common.Mapper<GoodSpec> {

    @Delete("DELETE from lm_goods_specs WHERE goods_id = #{id}")
    void deleteByGoodId(@Param("id") int id);

    @Select("SELECT * FROM lm_goods_specs where goods_id = #{id}")
    List<GoodSpec> findbyGoodId(@Param("id") int id);
}