package com.wink.livemall.goods.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wink.livemall.goods.dto.LmGoodMaterial;
import com.wink.livemall.goods.dto.LmLotsInfo;
import com.wink.livemall.goods.dto.User;
import org.springframework.stereotype.Repository;

@Mapper
public interface LotsInfoDao extends tk.mybatis.mapper.common.Mapper<LmLotsInfo>{
  
	@Select("select * from lm_lots_info where goodsid=#{goodsid} order by type asc,createtime desc  ")
	List<Map<String, Object>> findLotsInfo(@Param("goodsid")int goodsid );

	@Select("select * from lm_lots_info where goodsid=#{goodsid} and merchid=#{merchid} and type=#{type} limit 0,1")
	LmLotsInfo findLotsInfo2(@Param("goodsid")int goodsid, @Param("merchid")int merchid, @Param("type")int type);
}