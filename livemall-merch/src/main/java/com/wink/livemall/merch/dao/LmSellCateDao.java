package com.wink.livemall.merch.dao;


import com.wink.livemall.merch.dto.LmSellCate;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface LmSellCateDao extends tk.mybatis.mapper.common.Mapper<LmSellCate>{

	@Select("SELECT * FROM lm_sell_cate WHERE status = 1")
	List<LmSellCate> findlist();
   
}
