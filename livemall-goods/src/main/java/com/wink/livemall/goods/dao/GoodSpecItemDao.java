package com.wink.livemall.goods.dao;

import com.wink.livemall.goods.dto.GoodSpec;
import com.wink.livemall.goods.dto.GoodSpecItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodSpecItemDao extends tk.mybatis.mapper.common.Mapper<GoodSpecItem> {
    /**
     * 根据goodid删除
     * @param id
     */
    @Delete("DELETE from lm_goods_spec_items WHERE goods_id = #{id}")
    void deleteByGoodId(@Param("id") int id);

    /**
     * 根据goodid查询
     * @param id
     * @return
     */
    @Select("SELECT * FROM lm_goods_spec_items where goods_id = #{id}")
    List<GoodSpecItem> findByGoodId(@Param("id") int id);

    /**
     * 根据specid删除
     * @param id
     */
    @Delete("DELETE from lm_goods_spec_items WHERE spec_id = #{id}")
    void deleteBySpecid(@Param("id") int id);

    /**
     * 根据specid查询
     * @param id
     */
    @Select("SELECT * FROM lm_goods_spec_items where spec_id = #{id}")
    List<GoodSpecItem> findBySpecid(@Param("id") int id);

    @Select("SELECT title FROM lm_goods_spec_items where spec_id = #{id}")
    List<String> findtitleBySpecid(@Param("id") int id);
}