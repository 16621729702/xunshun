package com.wink.livemall.live.dao;

import com.wink.livemall.live.dto.LmLive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmLiveDao extends tk.mybatis.mapper.common.Mapper<LmLive>{

    @Select("SELECT ll.* , lm.store_name as store_name,lm.avatar as avatar FROM lm_lives ll left join lm_merch_info lm on ll.merch_id = lm.id where ll.categoryid = #{pid} and ll.status = 0  and ll.type = 0 order by ll.isstart desc,ll.watchnum desc")
    List<Map<String,Object>> findListByCategoryIdByApi(@Param("pid") int pid);



    @Select("SELECT ll.*, lm.store_name as store_name,lm.avatar as avatar FROM lm_lives ll left join lm_merch_info lm on ll.merch_id = lm.id " +
            " left join lm_member_follow lmf on ll.id = lmf.follow_id where ll.status = 0 and lmf.follow_type = 0 and ll.isstart = 1 and lmf.member_id =#{memberid} order by ll.isstart desc,ll.watchnum desc")
    List<Map<String,Object>> findfollewLiveByApi(@Param("memberid")int memberid);


    @Select("SELECT ll.*, lm.store_name as store_name,lm.avatar as avatar FROM lm_lives ll left join lm_merch_info lm on ll.merch_id = lm.id where lm.isauction = 1 and ll.status = 0 and ll.isstart = 1  limit 0,1")
    Map<String, Object> finddirectlyinfoByApi();

    @Select("SELECT ll.* , lm.store_name as store_name,lm.avatar as avatar FROM lm_lives ll left join lm_merch_info lm on ll.merch_id = lm.id where ll.type = 0 and ll.status = 0 and ll.isstart = 1 order by ll.isrecommend,ll.watchnum desc limit 0,2")
    List<Map<String,Object>> findhotlive();
    
    @Select("select * from lm_lives where merch_id=#{merch_id} limit 1")
    LmLive findLiveByMerchid(@Param("merch_id") int merch_id);

    @Select("SELECT ll.* , lm.store_name as store_name,lm.avatar as avatar FROM lm_lives ll left join lm_merch_info lm on ll.merch_id = lm.id " +
            " left join lm_member_follow lmf on ll.id = lmf.follow_id where ll.status = 0 and lmf.follow_type = 2 and ll.isstart = 1 and lmf.member_id =#{memberid} and ll.type = 1  ")
    List<Map<String, String>> findsharefollewLiveByApi(@Param("memberid")int memberid);

    @Select("SELECT ll.*, lm.store_name as store_name,lm.avatar as avatar FROM lm_lives ll left join lm_merch_info lm on ll.merch_id = lm.id where lm.categoryid = #{pid} and ll.status = 0 and ll.type = 1 ")
    List<Map<String, String>> findshareListByCategoryIdByApi(int pid);

    @Select("SELECT ll.* ,lm.store_name as store_name,lm.avatar as avatar " +
            " FROM lm_lives ll left join lm_merch_info lm on ll.merch_id = lm.id where ll.type = 1 and ll.isstart = 1 and ll.status = 0 order by ll.isrecommend,ll.watchnum desc limit 0,1")
    Map<String, Object> findsharehotlive();

    @Select("SELECT ll.* , lm.store_name as store_name,lm.avatar as avatar FROM lm_lives ll left join lm_merch_info lm on ll.merch_id = lm.id where  ll.status = 0 and ll.type = 0  order by ll.isrecommend,ll.watchnum desc limit 0,1")
    Map<String, String> findRecommendLiveByapi();

    @Select("SELECT ll.*, lm.store_name as store_name,lm.avatar as avatar FROM lm_lives ll left join lm_merch_info lm on ll.merch_id = lm.id where  ll.status = 0  and ll.type = 0 order by ll.isstart desc,ll.watchnum desc")
    List<Map<String,Object>> findHotLiveByApi();

    @Select("SELECT ll.* , lm.store_name as store_name,lm.avatar as avatar FROM lm_lives ll left join lm_merch_info lm on ll.merch_id = lm.id where  ll.status = 0 and ll.isstart = 1 and ll.type = 1  order by ll.isrecommend,ll.watchnum desc limit 0,1")
    Map<String, String> findShareRecommendLiveByapi();

    @Select("SELECT ll.* , lm.store_name as store_name,lm.avatar as avatar FROM lm_lives ll left join lm_merch_info lm on ll.merch_id = lm.id where  ll.status = 0  and ll.type = 1 order by ll.watchnum desc")
    List<Map<String, String>> findShareHotLiveByApi();

    @SelectProvider(type = sqlprovider.class, method = "findByCondient")
    List<Map<String, Object>> findByCondient(Map<String, String> condient);


    @SelectProvider(type = sqlprovider.class, method = "findListByNameByApi")
    List<Map<String, String>> findListByNameByApi(String name);

    @Select("select * from lm_lives where isstart = 1 ")
    List<LmLive> findLiveedLive();

    @Select("select * from lm_lives where status = 0 order by id desc")
    List<LmLive> findLiveList();

    @Select("select * from lm_lives where isstart = 2 and status = 0 ")
    List<LmLive> findLivePreview();

    @Select("select * from lm_lives order by isstart desc,isrecommend desc LIMIT 0,5")
    List<LmLive> findHotLive();

    class sqlprovider {

        public String findByCondient(Map<String, String> condient) {
            String sql = "SELECT " +
                    " ll.name as name, " +
                    " ll.id as id, " +
                    " ll.watchnum as watchnum, " +
                    " ll.img as img, " +
                    " ll.status as status, " +
                    " ll.isrecommend as isrecommend, " +
                    " ll.isstart as isstart, " +
                    " ll.livegroupid as livegroupid, " +
                    " lm.store_name as merchname, " +
                    " lc.name as categoryname " +
                    " from lm_lives ll,lm_merch_info lm,lm_live_category lc  where ll.categoryid = lc.id and ll.merch_id = lm.id ";
            if(!StringUtils.isEmpty(condient.get("name"))){
                sql += " and ll.name like '%"+condient.get("name")+"%'";
            }
            if(!StringUtils.isEmpty(condient.get("merchid"))){
                sql += " and ll.merch_id = "+condient.get("merchid");
            }
            sql +=" order by ll.id desc ";
            return sql;
        }

        public String findListByNameByApi(String name) {
            String sql ="SELECT ll.id as id," +
                    " ll.name as name," +
                    " ll.readurl as readurl," +
                    " ll.pushurl as pushurl," +
                    " ll.isstart as isstart," +
                    " ll.watchnum as watchnum," +
                    " ll.img as img," +
                    " lm.store_name as store_name," +
                    " lm.avatar as avatar " +
                    " FROM lm_lives ll left join lm_merch_info lm on ll.merch_id = lm.id where ll.status = 0 and ll.isstart = 1 " ;
            if(!StringUtils.isEmpty(name)){
                sql += " and ll.name like '%"+name+"%'";
            }
            sql+= " order by ll.watchnum desc";
            return sql;
        }
    }

}
