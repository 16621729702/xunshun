package com.wink.livemall.goods.dao;

import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LmGoodAuction;

import org.apache.ibatis.annotations.*;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface MerchGoodDao extends tk.mybatis.mapper.common.Mapper<Good> {
	@Select("SELECT * FROM lm_goods where id = #{id} ")
	Map<String, Object> findByid(@Param("id") String id);

	@Select("SELECT name FROM lm_warehouse where id in (#{ids}) ")
	List<Map<String, Object>> findWarehouses(@Param("ids") String ids);
	
	@Select("SELECT count(*) FROM lm_goods where mer_id = #{mer_id} and auction_end_time>#{auction_end_time}")
	int countAuctionGoods(@Param("mer_id") int mer_id,@Param("auction_end_time") Date auction_end_time);

	@Select("SELECT * FROM lm_good_auction where goodid = #{goodid} and status=1 limit 1 ")
	LmGoodAuction findLast(@Param("goodid") int goodid);

	@Select("SELECT count(*) FROM lm_goods where mer_id = #{mer_id} and isdelete =0 ")
	int countGoodsNum(@Param("mer_id") int mer_id);

	@Select("SELECT count(*) FROM lm_goods where mer_id = #{mer_id}  and type =#{type}  and state =#{state} and isdelete =0 ")
	int newCountGoodsNum(@Param("mer_id") int mer_id,@Param("type") int type,@Param("state") int state);

	@Select("SELECT count(*) FROM lm_orders where merchid = #{merchid}  ")
	int countOrderNum(@Param("merchid") int merchid);

	@Select("SELECT sum(realpayprice) FROM lm_orders where merchid = #{merchid} and status>0 ")
	Integer countOrderPrice(@Param("merchid") int merchid);

	@SelectProvider(type = sqlprovider.class, method = "updateByFields")
	void updateByFields(List<Map<String, String>> params, String id);

	@SelectProvider(type = sqlprovider.class, method = "addByFields")
	void addByFields(List<Map<String, String>> params);

	@SelectProvider(type = sqlprovider.class, method = "getpagesql")
	List<Map<String, Object>> getpage(Map<String, String> params);
	
	
	@SelectProvider(type = sqlprovider.class, method = "countByStatus")
	int countByStatus(Map<String, String> params);
	
	@SelectProvider(type = sqlprovider.class, method = "findListByCondition")
	List<Map<String, Object>> findListByCondition(Map<String, String> params);

	class sqlprovider {
		
		public String countByStatus(Map<String, String> params) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuilder sql = new StringBuilder();
			sql.append(" select count(*) from  lm_goods where isdelete = 0 and mer_id=" + params.get("merchid"));
			if (!StringUtils.isEmpty(params.get("status"))) {
				if (params.get("status").equals("1")) {
					sql.append(" and  auction_end_time > '" + sf.format(new Date()) + "'");
				} else if (params.get("status").equals("2")) {
					sql.append(" and  auction_end_time<'" + sf.format(new Date()) + "' and auction_status=1 ");
				} else if (params.get("status").equals("3")) {
					sql.append(" and auction_end_time<'" + sf.format(new Date()) + "' and auction_status=2 ");
				} else if (params.get("status").equals("4")) {
					sql.append(" and  auction_status=3 ");
				}
			}
			
			return sql.toString();
		}
		
		
		//type   不传值所有商品 1一口价商品 2直播商品 
		public String findListByCondition(Map<String, String> params) {
			
			StringBuilder sql = new StringBuilder();
			sql.append("select id,thumb,title,productprice,startprice from lm_goods where state = 1 and mer_id=" + params.get("merchid"));
			if(!StringUtils.isEmpty(params.get("type"))) {
				if(params.get("type").equals("1")) {
					sql.append(" and type=0 ");
				}else if(params.get("type").equals("2")) {
					sql.append(" and type=1 ");
				}
			}
			if(!StringUtils.isEmpty(params.get("keyword"))) {
				sql.append(" and (title like '%"+params.get("keyword")+"%'  )");
			}
			System.out.println(sql.toString());
			return sql.toString();
		}
		
		// 分页
		public String getpagesql(Map<String, String> params) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String pageindex = (String) params.get("pageindex");
			String pagesize = (String) params.get("pagesize");
			StringBuilder sql = new StringBuilder();
			sql.append("select g.state,g.auction_start_time,g.auction_end_time,g.label," +
					"c.name catename,g.thumb,g.id,g.material," +
					"g.place,g.spec,g.stock,g.title," +
					"g.bidsnum,g.type,"+
					"g.productprice,g.warehouse,g.sn" +
					" from lm_goods g left join lm_goods_categories c on g.category_id=c.id where g.isdelete = 0 and mer_id=" + params.get("merchid"));
			if (!StringUtils.isEmpty(params.get("warehouse"))) {
				if(params.get("warehouse").equals("2")){
					sql.append(" and g.state= 1 " );
				}else {
					sql.append(" and g.state= 0 " );
				}
			}
			if (!StringUtils.isEmpty(params.get("type"))) {
				sql.append(" and g.type=" + params.get("type"));
			}
			if (!StringUtils.isEmpty(params.get("category_id"))) {
				sql.append(" and g.category_id=" + params.get("category_id"));
			}
			if (!StringUtils.isEmpty(params.get("sn"))) {
				sql.append(" and g.sn like '%" + params.get("sn") + "%'");
			}
			if (!StringUtils.isEmpty(params.get("title"))) {
				sql.append(" and g.title like '%" + params.get("title") + "%'");
			}
			if (!StringUtils.isEmpty(params.get("price_start")) && !StringUtils.isEmpty(params.get("price_end"))) {
				sql.append(
						" and g.productprice between " + params.get("price_start") + " and " + params.get("price_end"));
			}
			if (!StringUtils.isEmpty(params.get("date"))) {
				String date_satrt = "";
				String date_end = "";
				if (params.get("date").equals("1")) {// 今天
					date_satrt = calc_date(0);
					date_end = calc_date(1);
				} else if (params.get("date").equals("2")) {// 近七天
					date_satrt = calc_date(-6);
					date_end = calc_date(1);
				} else if (params.get("date").equals("3")) {// 近30天
					date_satrt = calc_date(-29);
					date_end = calc_date(1);
				}
				sql.append(" and g.create_at between '" + date_satrt + "' and '" + date_end + "'");
			}
			if (!StringUtils.isEmpty(params.get("status"))) {
				if (params.get("status").equals("1")) {
					sql.append(" and  auction_end_time > '" + sf.format(new Date()) + "'");
				} else if (params.get("status").equals("2")) {
					sql.append(" and  auction_end_time<'" + sf.format(new Date()) + "' and auction_status=1 ");
				} else if (params.get("status").equals("3")) {
					sql.append(" and auction_end_time<'" + sf.format(new Date()) + "' and auction_status=2 ");
				} else if (params.get("status").equals("4")) {
					sql.append(" and  auction_status=3 ");
				}
			}

			if (!StringUtils.isEmpty(params.get("sort"))) {
				if (params.get("sort").equals("1")) {
					sql.append(" order by  g.create_at asc ");
				}
				if (params.get("sort").equals("2")) {
					sql.append(" order by  g.create_at desc ");
				}
			}else {
				sql.append(" order by g.update_at desc,g.id desc");
			}
			if (StringUtils.isEmpty(pageindex)) {
				pageindex = "1";
			}
			if (StringUtils.isEmpty(pagesize)) {
				pagesize = "10";
			}
			sql.append(" limit " + (Integer.parseInt(pageindex) - 1) * Integer.parseInt(pagesize) + ","
					+ Integer.parseInt(pagesize));

			System.out.println(sql.toString());
			return sql.toString();
		}

		public String updateByFields(List<Map<String, String>> params, String id) {
			StringBuilder builder = new StringBuilder();
			builder.append("update lm_goods set ");
			for (int i = 0; i < params.size(); i++) {
				Map<String, String> m = params.get(i);
				String field = m.get("field");
				String value = m.get("value");
				if (i == params.size() - 1) {
					if(value.equals("null")) {
						builder.append("" + field + "=" + value + "");
					}else {
						builder.append("" + field + "='" + value + "'");
					}
				} else {
					if(value.equals("null")) {
						builder.append("" + field + "=" + value + ",");
					}else {
						builder.append("" + field + "='" + value + "',");
					}
				}

			}

			builder.append(" where id=" + id);
			
			return builder.toString();
		}

		public String addByFields(List<Map<String, String>> params) {
			StringBuilder builder1 = new StringBuilder();
			builder1.append("insert into lm_goods(");
			String field=null;
			for (int i = 0; i < params.size(); i++) {
				Map<String, String> m = params.get(i);
				 field = m.get("field");
				if (i == params.size() - 1) {
					builder1.append("" + field );

				} else {
					builder1.append("" + field +  ",");
				}
			}
			builder1.append(") value");

			String value=null;
			for (int j = 0; j < params.size(); j++) {
				Map<String, String> m = params.get(j);
				 value = m.get("value");
				if (j == params.size() - 1) {
					builder1.append("'" + value +"'");

				} else {
					builder1.append("'" + value +  "',");
				}
			}
			builder1.append(")");
			return builder1.toString();
		}

		private String calc_date(int day) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.DAY_OF_MONTH, day);
			c1.set(Calendar.HOUR_OF_DAY, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 0);
			return sf.format(c1.getTime());
		}
	}
}