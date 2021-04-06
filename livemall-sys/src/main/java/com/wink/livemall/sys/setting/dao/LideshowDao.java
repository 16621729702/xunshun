package com.wink.livemall.sys.setting.dao;

import com.wink.livemall.sys.setting.dto.Express;
import com.wink.livemall.sys.setting.dto.Lideshow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface LideshowDao extends tk.mybatis.mapper.common.Mapper<Lideshow>{

    /**
     * 根据条件查询
     * @param condient
     * @return
     */
    @SelectProvider(type = LideshowDaoprovider.class, method = "findListByCondient")
    List<Lideshow> findByCondient(Map<String, String> condient);

    /**
     * 查询轮播图
     * @param type
     * @return
     */
    @Select("SELECT * FROM lm_setting_lideshows WHERE type = #{type} ORDER BY sort ")
    List<Lideshow> findListBytype(@Param("type")int type);

    /**
     * 查询轮播图NEW
     * @param type
     * @return
     */
    @SelectProvider(type = LideshowDaoprovider.class, method = "findListByTypeAndCategory")
    List<Lideshow> findListByTypeAndCategory(@Param("type")int type,@Param("category")int category);

    class LideshowDaoprovider{

        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT * from lm_setting_lideshows where 1=1 ";
            if(!StringUtils.isEmpty(condient.get("name"))){
                sql += " and name like '%"+condient.get("name")+"%'";
            }
            if(!StringUtils.isEmpty(condient.get("type"))){
                sql += " and type = '"+condient.get("type")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("wxappurl"))){
                sql += " and wxappurl = '"+condient.get("wxappurl")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("pic"))){
                sql += " and pic = '"+condient.get("pic")+"'";
            }
            if(!StringUtils.isEmpty(condient.get("sort"))){
                sql += " and sort = "+condient.get("sort")+"";
            }
            sql += " order by sort desc ";
            return sql;
        }

        public String findListByTypeAndCategory(@Param("type")int type,@Param("category")int category) {
            String sql = "SELECT * from lm_setting_lideshows WHERE type = #{type} ";
            if(type==3){
                sql += " and mer_category =  #{category} ";
            }else if(type==2){
                sql += " and live_category =  #{category} ";
            }
            sql += " order by sort ";
            return sql;
        }

    }
}
