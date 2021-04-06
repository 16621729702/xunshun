package com.wink.livemall.live.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.wink.livemall.live.dto.LmLiveLog;


@Mapper
public interface LmLiveLogDao extends tk.mybatis.mapper.common.Mapper<LmLiveLog> {

	@SelectProvider(type = sqlprovider.class, method = "findPage")
	List<LmLiveLog> findPage(Map<String, String> params);
	
	@Select("select * from lm_live_log where status=1 order by id desc limit 1")
	LmLiveLog findLastLog(@Param("merchid") String merchid);
	
	@SelectProvider(type = sqlprovider.class, method = "countTime")
	Long countTime(String merchid);

	@SelectProvider(type = sqlprovider.class, method = "countTimeList")
	Long countTimeList(String merId, String startTime, String entTime);

	@SelectProvider(type = sqlprovider.class, method = "liveLogNum")
	List<LmLiveLog> liveLogNum(String merId, String startTime, String entTime);

	@SelectProvider(type = sqlprovider.class, method = "liveLogTotal")
	Map<String, Object> liveLogTotal(String merId, String startTime, String entTime);


	class sqlprovider {
		
		public String countTime(String merchid) {
			String date_start=calc_date(-1);
    		String date_end=calc_date(0);
			
			StringBuilder sql=new StringBuilder();
			sql.append("select ifnull(sum(diff),0) from lm_live_log  where merchid="+merchid);
			sql.append(" and starttime between '"+date_start+"'"+" and "+"'"+date_end+"'");
			System.out.println(date_start);
			System.err.println(date_end);
			return sql.toString();
		}

		public String liveLogNum(String merId, String startTime, String entTime) {
			StringBuilder sql=new StringBuilder();
			sql.append("select * from lm_live_log  where merchid="+merId);
			sql.append(" and status=2 and endtime!='NULL' ");
			sql.append(" and starttime between '"+startTime+"'"+" and "+"'"+entTime+"'");
			return sql.toString();
		}

		public String liveLogTotal(String merId, String startTime, String entTime) {
			StringBuilder sql=new StringBuilder();
			sql.append("select ifnull(sum(diff),0) diffTotal, ifnull(sum(concurrent),0) concurrentTotal from lm_live_log  where merchid="+merId);
			sql.append(" and status=2 and endtime!='NULL' ");
			sql.append(" and starttime between '"+startTime+"'"+" and "+"'"+entTime+"'");
			return sql.toString();
		}

		public String countTimeList(String merId, String startTime, String entTime) {
			StringBuilder sql=new StringBuilder();
			sql.append("select ifnull(sum(diff),0) from lm_live_log  where merchid="+merId);
			sql.append(" and starttime between '"+startTime+"'"+" and "+"'"+entTime+"'");
			System.out.println(sql.toString());
			return sql.toString();
		}

		public String findPage(Map<String, String> params) {
    		StringBuilder sql=new StringBuilder();
    		String pageindex=(String) params.get("pageindex");
			String pagesize=(String) params.get("pagesize");
			
			String date_start=calc_date(-1);
    		String date_end=calc_date(0);
			sql.append("select * from lm_live_log  " );
			sql.append(" where merchid="+params.get("merchid"));
			sql.append(" and starttime between '"+date_start+"'"+" and "+"'"+date_end+"'");
			sql.append(" order by starttime desc ");
			
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
