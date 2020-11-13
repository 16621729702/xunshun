package com.wink.livemall.goods.dao;

import com.wink.livemall.goods.dto.LmGoodMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LmGoodMaterialDao extends tk.mybatis.mapper.common.Mapper<LmGoodMaterial>{

    @Select("select * from lm_good_material order by orderno desc ")
    List<LmGoodMaterial> findbyList();
}
