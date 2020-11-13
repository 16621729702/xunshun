package com.wink.livemall.coupon.dao;

import com.wink.livemall.coupon.dto.LmCoupons;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Priority;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface LmMerchCouponsDao extends tk.mybatis.mapper.common.Mapper<LmCoupons>{

	@SelectProvider(type = sqlprovider.class, method = "countenabled")
	int countEnabled(Map<String, String> params);
	
	@SelectProvider(type = sqlprovider.class, method = "findEnabled")
	List<Map<String, Object>> findEnabled(Map<String, String> params);
	
	class sqlprovider{
		public String countenabled(Map<String, String> params) {
			StringBuilder sql=new StringBuilder();
			sql.append("select count(*) from lm_coupons where merch_id="+params.get("merch_id"));
			sql.append(" and end_date>'"+params.get("date")+"'");
			sql.append(" and left_num>0 ");
		
			return sql.toString();
		}
		
		public String findEnabled(Map<String, String> params) {
			StringBuilder sql=new StringBuilder();
			sql.append("select id,num,start_date,end_date,left_num,use_num,useprice,rate from lm_coupons where merch_id="+params.get("merch_id"));
			sql.append(" and end_date>'"+params.get("date")+"'");
			sql.append(" and left_num>0 order by created_at desc  limit 3 ");
			System.out.println(sql.toString());
			return sql.toString();
		}
	}
	
}
