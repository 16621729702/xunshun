package com.wink.livemall.goods.dao;

import com.wink.livemall.goods.dto.LmShareGood;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface LmShareGoodDao extends tk.mybatis.mapper.common.Mapper<LmShareGood>{

    @Select("SELECT * FROM lm_share_good lg  where lg.status = 0  and lg.auction_end_time < #{nowdate} ")
    List<LmShareGood> findShareGood(@Param("nowdate")Date date);

    @Select("SELECT * FROM lm_share_good lg  where lg.status = 0  and lg.liveid = #{liveid} ")
	List<LmShareGood> findshareGoodByLiveid(@Param("liveid")int liveid);
}
