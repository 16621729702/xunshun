package com.wink.livemall.sys.msg.dao;


import com.wink.livemall.sys.msg.dto.LmPushmsg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmPushmsgDao extends tk.mybatis.mapper.common.Mapper<LmPushmsg> {

	@SelectProvider(type = sqlprovider.class, method = "getPageMerch")
	List<Map<String, Object>> getPageMerch(Map<String, String> params);


    class sqlprovider {

		// 分页
		public String getPageMerch(Map<String, String> params) {
			
			String pageindex = (String) params.get("pageindex");
			String pagesize = (String) params.get("pagesize");

			StringBuilder sql = new StringBuilder();
			sql.append("select p.*,m.avatar,m.nickname from lm_pushmsg p left join lm_member m on p.sendid=m.id where p.ismerch=1 and  recevieid=" + params.get("memberid"));
			sql.append(" order by p.createtime desc ");
			
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

	}



    @Select("SELECT " +
            " lp.sendid as sendid," +
            " lp.type as type," +
			" lp.content as content," +
			" lm.avatar as logo," +
			" lm.nickname as site_name," +
			" lp.createtime as createtime " +
            " FROM lm_pushmsg lp,lm_member lm WHERE lp.sendid = lm.id and lp.recevieid = #{receiveid} group by lp.sendid order by lp.createtime desc ")
    List<Map<String, Object>> getlistByreceiveid(@Param("receiveid") int receiveid);

    @Select("SELECT " +
            " lp.content as content," +
            " lp.createtime as createtime," +
			" lp.type as type," +
            " lp.isread as isread " +
            " FROM lm_pushmsg lp WHERE sendid = #{sendid} order by createtime desc ")
    List<Map<String, Object>> getlistBysendid(@Param("sendid") int sendid);

	@Select("SELECT " +
			" lp.content as content," +
			" lp.id as id," +
			" lp.createtime as createtime," +
			" lp.type as type," +
			" lm.avatar as logo," +
			" lm.nickname as site_name," +
			" lp.isread as isread " +
			" FROM lm_pushmsg lp,lm_member lm WHERE lp.sendid = lm.id and lp.sendid = #{sendid} and lp.recevieid = #{receiveid} order by createtime desc ")
	List<Map<String, Object>> getlistBysendidAndreceiveid(@Param("sendid")int sendid, @Param("receiveid")int receiveid);

}
