package com.wink.livemall.sys.setting.dao;

import com.wink.livemall.sys.setting.dto.Tag;
import com.wink.livemall.sys.setting.dto.Templates;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface TemplatesDao extends  tk.mybatis.mapper.common.Mapper<Templates>{

    /**
     * 根据条件查询商品
     * @param condient
     * @return
     */
    @SelectProvider(type = TemplatesDaoprovider.class, method = "findListByCondient")
    List<Templates> findByCondient(Map<String, String> condient);


    class TemplatesDaoprovider{
        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT * from lm_setting_templates where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("name"))){
                sql += " and name like '%"+condient.get("name")+"%'";
            }
            if(!StringUtils.isEmpty(condient.get("status"))){
                sql += " and status = "+condient.get("status");
            }
            return sql;
        }
    }
}
