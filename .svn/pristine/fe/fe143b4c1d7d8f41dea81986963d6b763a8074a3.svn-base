package com.wink.livemall.merch.dao;



import com.wink.livemall.merch.dto.LmMerchBlack;
import com.wink.livemall.merch.dto.LmMerchFollow;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;


@Mapper
public interface LmMerchBlackDao extends tk.mybatis.mapper.common.Mapper<LmMerchBlack>{

	@SelectProvider(type =sqlProvider.class,method = "countsql" )
	int countByCondition(Map<String, Object> params);
	
	@SelectProvider(type =sqlProvider.class,method = "getpagesql" )
	List<Map<String, Object>> getpage(Map<String, Object> params);
	
	
	/**
	 * @author Administrator
	 *分页查询
	 */
	class sqlProvider{
		public String getpagesql(Map< String, Object> params) {
			System.out.println("merchid"+params.get("merchid"));
			String pageindex=(String) params.get("pageindex");
			String pagesize=(String) params.get("pagesize");
			
			StringBuilder sql=new StringBuilder();
			sql.append("select a.merchid merchid,b.nickname,b.avatar,b.id memberid,b.realname,b.mobile from lm_merch_black a left join lm_member b on a.memberid=b.id where type=1 and merchid="+params.get("merchid"));
		
			sql.append(" order by a.createtime desc ");
			if(StringUtils.isEmpty(pageindex)) {
				pageindex="1";
			}
			if(StringUtils.isEmpty(pagesize)) {
				pagesize="10";
			}
			sql.append(" limit "+(Integer.parseInt(pageindex)-1)*Integer.parseInt(pagesize)+","+Integer.parseInt(pagesize));
		
			
			return sql.toString();
		}
		
		public String countsql(Map< String, Object> params) {
			StringBuilder sql=new StringBuilder();
			sql.append("select count(*) from lm_merch_black where 1 ");
			
			if(!StringUtils.isEmpty(params.get("memberid"))) {
				sql.append(" and memberid="+params.get("memberid"));
			}
			if(!StringUtils.isEmpty(params.get("merchid"))) {
				sql.append(" and merchid="+params.get("merchid"));
			}
			if(!StringUtils.isEmpty(params.get("type"))) {
				sql.append(" and type="+params.get("type"));
			}
			return sql.toString();
		
		}
		
		
	}
   
}
