package com.wink.livemall.sys.setting.dao;

import com.wink.livemall.sys.role.dto.Role;
import com.wink.livemall.sys.setting.dto.Express;
import org.apache.ibatis.annotations.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExpressDao extends tk.mybatis.mapper.common.Mapper<Express>{

    /**
     * 根据条件查询商品
     * @param condient
     * @return
     */
    @SelectProvider(type = ExpressDaoprovider.class, method = "findListByCondient")
    List<Express> findByCondient(Map<String, String> condient);

    @Select("SELECT * FROM lm_setting_expresses WHERE is_show = '1'")
    List<Express> findActiveList();


    class ExpressDaoprovider{

        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT * from lm_setting_expresses where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("name"))){
                sql += " and name like '%"+condient.get("name")+"%'";
            }
            if(!StringUtils.isEmpty(condient.get("code"))){
                sql += " and code = '"+condient.get("code")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("categoryid"))){
                sql += " and is_show = '"+condient.get("is_show")+"'";
            }
            sql += " order by sort desc ";
            return sql;
        }
    }
}
