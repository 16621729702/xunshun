package com.wink.livemall.merch.dao;



import com.wink.livemall.merch.dto.LmMerchFollow;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;


@Mapper
public interface LmMerchFollowDao extends tk.mybatis.mapper.common.Mapper<LmMerchFollow>{

	@SelectProvider(type =LmMerchFollowDaoProvider.class,method = "getpagesql" )
	List<Map<String, Object>> getpage(Map<String, Object> params);
	
	
	/**
	 * @author Administrator
	 *粉丝分页查询
	 */
	class LmMerchFollowDaoProvider{
		public String getpagesql(Map< String, Object> params) {
			System.out.println("merchid"+params.get("merchid"));
			String pageindex=(String) params.get("pageindex");
			String pagesize=(String) params.get("pagesize");
			StringBuilder sql=new StringBuilder();
			sql.append("select a.merchid merchid,b.nickname,b.avatar,b.id memberid,b.realname,b.mobile from lm_merch_follow a left join lm_member b on a.memberid=b.id where follow_id="+params.get("merchid"));
			
			//昨日
			String date_start=calc_date(-1);
    		String date_end=calc_date(0);
			if(!StringUtils.isEmpty(params.get("day"))) {
				if(params.get("day").equals("-1")) {
					sql.append(" and a.follow_time between "+date_start+" and "+date_end);
				}
			}
			sql.append(" order by a.createtime desc ");
			if(StringUtils.isEmpty(pageindex)) {
				pageindex="1";
			}
			if(StringUtils.isEmpty(pagesize)) {
				pagesize="10";
			}
			sql.append(" limit "+(Integer.parseInt(pageindex)-1)*Integer.parseInt(pagesize)+","+Integer.parseInt(pagesize));
		
			System.out.println(sql.toString());
			return sql.toString();
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
