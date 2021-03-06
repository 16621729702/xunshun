package com.wink.livemall.live.dao;


import com.wink.livemall.live.dto.LmLiveGood;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

@Mapper
public interface LmLiveGoodDao extends tk.mybatis.mapper.common.Mapper<LmLiveGood>{
	@SelectProvider(type = sqlprovider.class, method = "getpagesql")
	List<Map<String, Object>> getpage(Map<String, String> params);


	@Select("select lg.name as name," +
			" lg.chipped_num as num," +
			" lg.img as img," +
			" lg.id as id," +
			" lg.chipped_price as price," +
			" lg.auction_start_time as starttime," +
			" lg.auction_end_time as endtime" +
			"  from lm_share_good lg where  lg.liveid = #{liveid} and lg.status = 0 order by lg.create_at desc ")
	List<Map<String, Object>> findshareByLiveIdByApi(@Param("liveid")int liveid);

	@Select("select lg.title as name," +
			" lg.id as id," +
			" lg.thumb as img," +
			" lg.startprice as startprice," +
			" lg.productprice as productprice, " +
			" lg.auction_start_time as auction_start_time, " +
			" lg.auction_end_time as auction_end_time, " +
			" lg.stepprice as stepprice " +
			"  from lm_live_good llg , lm_goods lg where llg.good_id = lg.id and llg.liveid = #{liveid} and lg.type = #{type} and lg.state = 1 and lg.stock > 0 ")
    List<Map<String, Object>> findByLiveIdByApi(@Param("liveid")int liveid, @Param("type")int type);

	@Select("select llg.id as id ," +
			" lg.auction_end_time as auction_end_time , lg.delaytime as delaytime from lm_live_good llg,lm_goods lg where llg.good_id = lg.id ")
	List<Map<String, Object>> findAllInfo();

	@SelectProvider(type = sqlprovider.class, method = "findInfoByLiveidAndName")
	List<Map<String, Object>> findInfoByLiveidAndName(Map<String, String> condient);

	@Select("select * from lm_live_good where liveid = #{liveid} ")
	List<LmLiveGood> findByLiveid(@Param("liveid")int liveid);

	@Select("select * from lm_live_good where liveid = #{liveid}  and good_id = #{goodid}")
	LmLiveGood checkGood(@Param("goodid")int goodid, @Param("liveid")int liveid);


	@Select("select lg.id,g.thumb,g.title,g.productprice,g.startprice from lm_live_good lg left join lm_goods g on lg.good_id=g.id  where lg.liveid = #{liveid} ")
	List<Map<String, Object>> findLiveGoodByLiveid(@Param("liveid")int liveid);

	@Select("select * from lm_livegood  where liveid = #{liveid} and type = #{type} and status = 0 order by  starttime desc ")
	List<Map<String, Object>> findLivegoodinfoById(@Param("liveid")int liveid, @Param("type")int type);

	@Select("select * from lm_livegood  where liveid = #{liveid} and type = #{type} and status = 0 and tomemberid = #{userid} order by  starttime desc ")
	List<Map<String, Object>> findLivegoodtomemberid(@Param("liveid")int liveid, @Param("type")int type, @Param("userid")int userid);

	@Select("select * from lm_livegood  where good_id = #{good_id}  ")
	List<LmLiveGood> findlivegoodByGoodid(@Param("good_id")int good_id);


    class sqlprovider {
		public String getpagesql(Map<String, String> params) {
			StringBuilder sql=new StringBuilder();
			sql.append("select lg.id,g.thumb,g.title,g.productprice,g.startprice from lm_live_good lg left join lm_goods g on lg.good_id=g.id where lg.liveid="+params.get("liveid"));
			sql.append(" order by lg.id desc ");
			
			String pageindex = (String) params.get("pageindex");
			String pagesize = (String) params.get("pagesize");
			if (StringUtils.isEmpty(pageindex)) {
				pageindex = "1";
			}
			if (StringUtils.isEmpty(pagesize)) {
				pagesize = "10";
			}
			sql.append(" limit " + (Integer.parseInt(pageindex) - 1) * Integer.parseInt(pagesize) + ","+ Integer.parseInt(pagesize));
			System.out.println(sql.toString());
			return sql.toString();
		}
		public String findInfoByLiveidAndName(Map<String, String> params) {
			String sql = "select" +
					" lg.id as id," +
					" lg.title as name," +
					" lg.thumb as img," +
					" lg.productprice as price," +
					" lg.stock as stock," +
					" lg.state as status" +
					" from lm_live_good llg ,lm_goods lg where llg.good_id = lg.id ";
			if(!StringUtils.isEmpty(params.get("liveid"))){
				sql += " and llg.liveid = "+params.get("liveid");
			}
			if(!StringUtils.isEmpty(params.get("name"))){
				sql += " and lg.title like '%"+params.get("name")+"%' ";
			}
			return sql;
		}

	}
	
}



