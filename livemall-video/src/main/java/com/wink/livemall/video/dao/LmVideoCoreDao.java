package com.wink.livemall.video.dao;

import com.wink.livemall.video.dto.LmVideoCore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Mapper
public interface LmVideoCoreDao extends tk.mybatis.mapper.common.Mapper<LmVideoCore> {

	@SelectProvider(type = LmVideoCoreDaoprovider.class, method = "findListByCondient")
	List<Map> findByCondient(Map<String, String> condient);

    @Select("SELECT lvc.id as id,lvc.img as img,lvc.video as video, lvc.name as name,lvc.content as content ," +
            "lvc.tag as tag,lvc.category as category,lvc.good_id as good_id,lvc.playnum as playnum," +
            "lm.avatar as avatar,lm.nickname as nickname FROM lm_video_core lvc left join lm_member lm on lvc.creatuserid = lm.id where istop = 0 ")
    List<Map<String,String>> findTopVideo();

    @Select("SELECT lvc.id as id,lvc.img as img,lvc.video as video, lvc.name as name,lvc.content as content , " +
            " lvc.tag as tag,lvc.category as category,lvc.good_id as good_id,lvc.playnum as playnum," +
            " lm.avatar as avatar,lm.nickname as nickname FROM lm_video_core lvc left join lm_member lm on lvc.creatuserid = lm.id order by playnum desc ")
    List<Map<String,String>> findHotVideolist();

    @Select("SELECT * FROM lm_video_core where category = ${category} order by playnum desc")
    List<LmVideoCore> findByCategoryId(@Param("category") int category);

    @Select("SELECT * FROM lm_video_core  where category = ${categoryid} order by playnum desc ")
    List<LmVideoCore> findByTag(@Param("categoryid")String categoryid);

    @Select("SELECT lvc.id as id,lvc.img as img,lvc.video as video, lvc.name as name,lvc.content as content , " +
            " lvc.tag as tag,lvc.category as category,lvc.good_id as good_id,lvc.playnum as playnum," +
            " lm.avatar as avatar,lm.nickname as nickname FROM lm_video_core lvc left join lm_member lm on lvc.creatuserid = lm.id where lvc.category = ${categoryid} order by playnum desc ")
    List<Map<String, String>> findByCategoryIdByApi(@Param("categoryid") int categoryid);

	@SelectProvider(type = LmVideoCoreDaoprovider.class, method = "updateByFields")
	void updateByFields(List<Map<String, String>> params, String id);

	@SelectProvider(type = LmVideoCoreDaoprovider.class, method = "getpagesql")
	List<LmVideoCore> getpage(Map<String, String> params);

    class LmVideoCoreDaoprovider{

		// 分页
		public String getpagesql(Map<String, String> params) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String pageindex = (String) params.get("pageindex");
			String pagesize = (String) params.get("pagesize");

			StringBuilder sql = new StringBuilder();
			sql.append("select * from lm_video_core where creatuserid=" + params.get("userid"));
			if (!StringUtils.isEmpty(params.get("category"))) {
				sql.append(" and  category="+params.get("category"));
			}

			sql.append(" order by creattime desc ");
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
			builder.append("update lm_video_core set ");
			for (int i = 0; i < params.size(); i++) {
				Map<String, String> m = params.get(i);
				String field = m.get("field");
				String value = m.get("value");
				if (i == params.size() - 1) {
					builder.append("" + field + "='" + value + "'");

				} else {
					builder.append("" + field + "='" + value + "',");

				}

			}

			builder.append(" where id=" + id);
			return builder.toString();
		}

		public String findListByCondient(Map<String, String> condient) {
			String sql = "SELECT " + " vc.id as id," + " vc.name as name," + " vc.tag as tag,"
					+ " vc.content as content," + " vc.img as img," + " vc.good_id as goodids,"
					+ " vca.name as categoryname " + " from lm_video_core vc "
					+ " left join lm_video_category vca on vc.category = vca.id " + " where 1=1 ";
			if (!StringUtils.isEmpty(condient.get("name"))) {
				sql += " and vc.name like '%" + condient.get("name") + "%'";
			}
			return sql;
		}
	}
}
