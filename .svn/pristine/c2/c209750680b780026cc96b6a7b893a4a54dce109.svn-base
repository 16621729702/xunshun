package com.wink.livemall.goods.dao;

import com.wink.livemall.goods.dto.GoodSpecItem;
import com.wink.livemall.goods.dto.GoodSpecOption;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodSpecOptionDao extends tk.mybatis.mapper.common.Mapper<GoodSpecOption> {

    @Delete("DELETE from lm_goods_spec_options WHERE find_in_set(#{ids},spec_item_ids) ")
    void deleteByGoodId(@Param("ids") String ids);

    @Select("select * from lm_goods_spec_options where goods_id = #{id}")
    List<GoodSpecOption> findByGoodid(@Param("id") int id);

    @Delete("DELETE from lm_goods_spec_options WHERE goods_id = #{goodid}")
    void deleteByGoodid(@Param("goodid") int goodid);
}