package com.wink.livemall.sys.image.dao;

import com.wink.livemall.sys.image.dto.LmImageCore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmImageCoreDao extends tk.mybatis.mapper.common.Mapper<LmImageCore>{

    @SelectProvider(type = LmImageCoreDaoprovider.class, method = "findListByCondient")
    List<LmImageCore> findByCondient(Map<String, Object> condient);


    class LmImageCoreDaoprovider{

        public String findListByCondient(Map<String, Object> condient) {
            String sql = "SELECT * from lm_image_core where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("groupid"))){
                sql += " and group_id = "+condient.get("groupid");
            }
            if(!StringUtils.isEmpty(condient.get("startdate"))){
                sql += " and createtime >= '"+condient.get("startdate")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("enddate"))){
                sql += " and createtime <= '"+condient.get("enddate")+"'";
            }
            sql+=" order by createtime desc ";
            return sql;
        }

    }
}
