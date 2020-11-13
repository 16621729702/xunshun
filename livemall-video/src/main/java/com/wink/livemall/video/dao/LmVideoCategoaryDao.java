package com.wink.livemall.video.dao;

import com.wink.livemall.video.dto.LmVideoCategoary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmVideoCategoaryDao extends tk.mybatis.mapper.common.Mapper<LmVideoCategoary>{

    @SelectProvider(type = LmVideoCategoaryDaoprovider.class, method = "findListByCondient")
    List<Map> findByCondient(Map<String, String> condient);

    @Select("SELECT * FROM lm_video_category where pid = 0 and status = 0")
    List<LmVideoCategoary> findtopcategory();

    @Select("SELECT name as name ,id as id FROM lm_video_category where pid = 0 and status = 0")
    List<Map<String, String>> findtopcategoryByApi();

    class LmVideoCategoaryDaoprovider{

        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT " +
                    " vc.id as id, " +
                    " vc.pid as pid, " +
                    " vc.name as name, " +
                    " vc.orderno as orderno," +
                    " vc.status as status," +
                    " vca.name as pname from lm_video_category vc left join lm_video_category vca on vc.pid = vca.id where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("name"))){
                sql += " and vc.name like '%"+condient.get("name")+"%'";
            }
            if(!StringUtils.isEmpty(condient.get("pid"))){
                sql += " and vc.pid ="+condient.get("pid");
            }
            sql +=" order by vc.orderno desc";
            return sql;
        }

    }
}
