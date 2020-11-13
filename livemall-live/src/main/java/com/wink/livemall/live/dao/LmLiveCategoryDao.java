package com.wink.livemall.live.dao;

import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.dto.LmLiveCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Mapper
public interface LmLiveCategoryDao extends tk.mybatis.mapper.common.Mapper<LmLiveCategory>{


    @Select("SELECT * FROM lm_live_category where pid = 0 and status = 0")
    List<LmLiveCategory> findTopListByApi();

    @SelectProvider(type = sqlprovider.class, method = "findByCondient")
    List<LmLiveCategory> findByCondient(Map<String, String> condient);


    class sqlprovider {

        public String findByCondient(Map<String, String> condient) {
            String sql = "SELECT * from lm_live_category   where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("name"))){
                sql += " and name like '%"+condient.get("name")+"%'";
            }
            sql +=" order by orderno desc";
            return sql;
        }
    }

}
