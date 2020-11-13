package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.LmMemberFollow;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Mapper
public interface LmMemberFollowDao extends tk.mybatis.mapper.common.Mapper<LmMemberFollow> {

	@Select("SELECT * FROM lm_member_follow WHERE member_id = #{memberid}")
	List<LmMemberFollow> findByMemberid(@Param("memberid") int memberid);

	@SelectProvider(type = LmMemberFollowDaoprovider.class, method = "findMerchInfoByUserid")
	List<Map<String, Object>> findMerchInfoByUserid(@Param("memberid")int userid);

	@SelectProvider(type = LmMemberFollowDaoprovider.class, method = "countNumYed")
	int countNumYed(String merchid);

	@SelectProvider(type = LmMemberFollowDaoprovider.class, method = "getpagesql")
	List<Map<String, Object>> getpage(Map<String, Object> params);

	@SelectProvider(type = LmMemberFollowDaoprovider.class, method = "findLiveinfofoByUserid")
	List<Map<String, Object>> findLiveinfofoByUserid(@Param("memberid")int userid);

	@Select("SELECT * FROM lm_member_follow WHERE member_id = #{userid} and follow_id = #{merchid} and state = 0 and follow_type = 1 limit 0,1")
	LmMemberFollow findByMemberidAndMerchId(@Param("userid")int userid, @Param("merchid")int merchid);

	@Select("SELECT * FROM lm_member_follow WHERE member_id = #{userid} and follow_id = #{id} and state = 0 and follow_type = #{type} limit 0,1")
    LmMemberFollow findByMemberidAndTypeAndId(@Param("userid")int userid, @Param("type")int type, @Param("id")int id);

	@Select("SELECT * FROM lm_member_follow WHERE follow_id = #{merchid} and state = 0 and follow_type = #{type}")
	List<LmMemberFollow> findByMerchidAndType(@Param("type")int type, @Param("merchid")int merchid);

    class LmMemberFollowDaoprovider {

    	public String countNumYed(String merchid) {
    		StringBuilder sql = new StringBuilder();
    		String date_start=calc_date(-1);
    		String date_end=calc_date(0);
    		sql.append("select count(id) from lm_member_follow where follow_id="+merchid );
    		sql.append(" and follow_time between '"+date_start+"'"+" and "+"'"+date_end+"'");
    		System.out.println(sql.toString());
    		return sql.toString();
		}
    	
		public String findMerchInfoByUserid(@Param("memberid")int userid) {
			String sql = "SELECT " +
					" lm.id as id," +
					" lm.avatar as avatar," +
					" lmf.id as followid," +
					" lm.label as label ," +
					" lm.isauction as isauction ," +
					" lm.isdirect as isdirect ," +
					" lm.isquality as isquality ," +
					" lm.postage as postage ," +
					" lm.refund as refund ," +
					" lm.isoem as isoem ," +
					" lm.store_name as store_name"
					+ " from lm_member_follow lmf , lm_merch_info lm where lmf.follow_id = lm.id  "
					+ " and lmf.follow_type = 1 and lmf.state = 0 and lmf.member_id = #{memberid}" ;
			sql += " order by follow_time desc ";
			return sql;
		}
		public String findLiveinfofoByUserid(@Param("memberid")int userid) {
			String sql = "select " +
					" ll.name as livename," +
					" ll.id as id," +
					" ll.type as type," +
					" lmf.follow_id as followid," +
					" ll.img as liveimg," +
					" ll.isstart as isstart," +
					" lm.store_name as store_name," +
					" lm.avatar as avatar," +
					" ll.watchnum as watchnum" +
					" from lm_member_follow lmf ,lm_lives ll,lm_merch_info lm where lmf.follow_id = ll.id and ll.merch_id = lm.id " +
					" and lmf.member_id = #{memberid} and lmf.follow_type = 0 and lmf.state = 0";
			return sql;
		}

		public String getpagesql(Map<String, Object> params) {
			System.out.println("merchid" + params.get("merchid"));
			String pageindex = (String) params.get("pageindex");
			String pagesize = (String) params.get("pagesize");
			StringBuilder sql = new StringBuilder();
			sql.append(
					"select l.name levelname,l.code levelcode,a.follow_id merchid,b.nickname,b.avatar,b.id memberid,b.realname,b.mobile from lm_member_follow a left join lm_member b on a.member_id=b.id"
					+ "	left join lm_member_level l on l.id=b.level_id where follow_id="
							+ params.get("merchid"));
			//昨日
			String date_start=calc_date(-1);
    		String date_end=calc_date(0);
			if(!StringUtils.isEmpty(params.get("day"))) {
				if(params.get("day").equals("-1")) {
					sql.append(" and a.follow_time between '"+date_start+"' and '"+date_end+"'");
				}
			}
			
			sql.append(" order by a.follow_time desc ");
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
