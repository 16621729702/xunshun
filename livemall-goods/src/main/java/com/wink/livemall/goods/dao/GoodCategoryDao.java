package com.wink.livemall.goods.dao;

import com.wink.livemall.goods.dto.GoodCategory;
import com.wink.livemall.goods.dto.User;
import org.apache.ibatis.annotations.*;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface GoodCategoryDao extends tk.mybatis.mapper.common.Mapper<GoodCategory>{
    /**
     * 通过名字查询用户信息
     */
    @Select("SELECT * FROM lm_goods_categories WHERE name = #{name}")
    GoodCategory findGoodCategoryByName(@Param("name") String name);


    /**
     * 按条件查询分类信息
     * @param condient
     * @return
     */
    @SelectProvider(type = GoodCategoryDaoprovider.class, method = "findListByCondient")
    List<GoodCategory> findGoodCategoryByCondient(Map<String, String> condient);

    /**
     * 查询所有顶级分类
     * @return
     */
    @Select("SELECT * FROM lm_goods_categories where parent_id = 0 and isshow = 0")
    List<GoodCategory> findtopgoodscategory();

    /**
     * 根据id查询分类信息
     * @param id
     * @return
     */
    @Select("SELECT * FROM lm_goods_categories where id = #{id}")
    GoodCategory findgoodscategoryById(@Param("id")int id);

    @Select("SELECT * FROM lm_goods_categories where isshow = 0")
    List<GoodCategory> findActiveList();

    @Select("SELECT * FROM lm_goods_categories where parent_id = #{pid} and isshow = 0")
    List<GoodCategory> findByPid(@Param("pid")int pid);

    @Select("SELECT * FROM lm_goods_categories where isrecommend = 1 and isshow = 0")
    List<GoodCategory> findrecommend();

    @Select("SELECT id as id ,name as name,pic as pic FROM lm_goods_categories where parent_id = 0 and isshow = 0 order by sort ")
    List<Map<String, String>> findtopgoodscategorybyapi();

    @Select("SELECT id as id ,name as name,pic as pic,parent_id as parent_id FROM lm_goods_categories where isrecommend = 1 and isshow = 0")
    List<Map<String, String>> findrecommendByapi();

    @Select("SELECT id as id ,name as name,pic as pic,parent_id as parent_id FROM lm_goods_categories where parent_id = #{pid} and isshow = 0")
    List<Map<String, String>> findByPidByapi(@Param("pid")int pid);


    class GoodCategoryDaoprovider{

        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT * from lm_goods_categories  where 1=1 ";
            if(condient.get("name")!=null){
                sql += " and name like '%"+condient.get("name")+"%'";
            }
            if(condient.get("pid")!=null){
                sql += " and parent_id = "+condient.get("pid");
            }
            sql += " order by sort desc ";
            return sql;
        }

        public String updatecategories( int id, String name, Integer parent_id, String pic, String home_pic, String intro, String sort, String isshow, String updated_at){
            String sql = "UPDATE  lm_goods_categories SET";
            if(!StringUtils.isEmpty(name)){
                sql += " name = '"+name+"',";
            }
            if(!StringUtils.isEmpty(parent_id)){
                sql += " parent_id = '"+parent_id+"',";
            }
            if(!StringUtils.isEmpty(pic)){
                sql += " pic = '"+pic+"',";
            }
            if(!StringUtils.isEmpty(home_pic)){
                sql += " home_pic = '"+home_pic+"',";
            }
            if(!StringUtils.isEmpty(intro)){
                sql += " intro = '"+intro+"',";
            }
            if(!StringUtils.isEmpty(sort)){
                sql += " sort = '"+sort+"',";
            }
            if(!StringUtils.isEmpty(isshow)){
                sql += " isshow = '"+isshow+"',";
            }
            if(!StringUtils.isEmpty(updated_at)){
                sql += " updated_at = '"+updated_at+"',";
            }
            sql = sql.substring(0,sql.length()-1);
            sql+=" WHERE id = "+id;
            return sql;
        }
    }
}