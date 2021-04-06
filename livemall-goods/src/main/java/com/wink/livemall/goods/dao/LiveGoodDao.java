package com.wink.livemall.goods.dao;

import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.goods.dto.LmShareGood;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface LiveGoodDao extends tk.mybatis.mapper.common.Mapper<LivedGood>{

    @Select("SELECT * FROM lm_livegood lg  where lg.status = 0  and lg.liveid = #{liveid} ")
    List<LivedGood> findLivedGoodByLiveid(@Param("liveid") int liveid);

    @Select("SELECT * FROM lm_livegood lg  where lg.status = 0  and lg.endtime < #{nowdate} ")
    List<LivedGood> findShareGood(@Param("nowdate")Date date);

    @Select("select * from lm_livegood  where liveid = #{liveid}  and type = 1 and status = 0 order by id desc ")
    List<LivedGood> movementImLive(@Param("liveid")int liveid);

    @Select("SELECT * FROM lm_livegood   where id= #{id}")
    LivedGood findById(@Param("id")int id);

}
