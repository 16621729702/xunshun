package com.wink.livemall.sys.setting.dao;

import com.wink.livemall.sys.setting.dto.Express;
import com.wink.livemall.sys.setting.dto.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
@Mapper
public interface TagDao  extends  tk.mybatis.mapper.common.Mapper<Tag>{



    /**
     * 根据条件查询商品
     * @param condient
     * @return
     */
    @SelectProvider(type = TagDaoprovider.class, method = "findListByCondient")
    List<Tag> findByCondient(Map<String, String> condient);


    class TagDaoprovider{
        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT * from lm_setting_tags where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("name"))){
                sql += " and name like '%"+condient.get("name")+"%'";
            }
            if(!StringUtils.isEmpty(condient.get("title"))){
                sql += " and title = '"+condient.get("title")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("type"))){
                sql += " and type = "+condient.get("type")+"";
            }
            if(!StringUtils.isEmpty(condient.get("style"))){
                sql += " and style = "+condient.get("style")+"";
            }
            sql += " order by sort desc ";
            return sql;
        }
    }
}
