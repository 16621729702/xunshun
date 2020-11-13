package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.LmMemberCoupon;
import com.wink.livemall.member.dto.LmMemberFav;
import com.wink.livemall.member.dto.LmMemberFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmMemberFavDao  extends tk.mybatis.mapper.common.Mapper<LmMemberFav> {

    @Select("SELECT * FROM lm_member_fav WHERE member_id = #{memberid}")
    List<LmMemberFav> findByMemberid(@Param("memberid")int memberid);

    @SelectProvider(type = LmMemberFavDaoprovider.class, method = "findListByCondient")
    List<Map<String, String>> findInfoByMemberid(@Param("userid")int userid);

    @Select("SELECT * FROM lm_member_fav WHERE member_id = #{userid} and goods_id = #{goodid} and state = 0 limit 0,1")
    LmMemberFav findByMemberidAndGoodid(@Param("userid")int userid, @Param("goodid")int goodid);

    @Select("SELECT * FROM lm_member_fav WHERE member_id = #{userid} and video_id = #{videoid} and state = 0")
    LmMemberFav findByMemberidAndVideoidByApi(@Param("userid")int menberid, @Param("videoid")int videoid);

    @SelectProvider(type = LmMemberFavDaoprovider.class, method = "findInfoByMemberidByApi")
    List<Map<String, String>> findInfoByMemberidByApi(@Param("userid")int userid, @Param("type")int type);

    @SelectProvider(type = LmMemberFavDaoprovider.class, method = "findByMemberidAndTypeAndId")
    LmMemberFav findByMemberidAndTypeAndId(@Param("userid")int userid, @Param("type")int type, @Param("id")int id);

    class LmMemberFavDaoprovider{

        public String findInfoByMemberidByApi(@Param("userid")int userid,@Param("type")int type) {
            String sql="";
            if(type==1){
                sql+= "SELECT " +
                        " lg.title as goodname," +
                        " lg.thumb as img," +
                        " lg.id as id," +
                        " lmt.id as favid," +
                        " lg.productprice as price" +
                        " from lm_member_fav lmt , lm_goods lg  where lmt.goods_id = lg.id  " +
                        " and lmt.member_id = #{userid}";
                sql+=" and lmt.state = 0 ";
                sql+=" order by create_time desc ";
            }else{
                sql+= "SELECT " +
                        " lv.name as videoname," +
                        " lm.nickname as nickname," +
                        " lm.avatar as avatar," +
                        " lv.img as img," +
                        " lmt.id as favid," +
                        " lv.id as id," +
                        " lv.playnum as playnum" +
                        " from lm_member_fav lmt, lm_video_core lv,lm_member lm  where lmt.video_id = lv.id and lmt.member_id = lm.id " +
                        "  and lmt.member_id = #{userid}";
                sql+=" and lmt.state = 0 ";
                sql+=" order by create_time desc ";
            }
            return sql;
        }

        public String findListByCondient(@Param("userid")int userid) {
            String sql="";
            sql+= "SELECT " +
                    " lg.title as goodname," +
                    " lg.thumb as img," +
                    " lg.id as id," +
                    " lg.productprice as price" +
                    " from lm_member_fav lmt left join lm_goods lg  on lmt.goods_id = lg.id where " +
                    " lmt.member_id = #{userid}";
            sql+=" and lmt.state = 0 ";
            sql+=" order by create_time desc ";

            return sql;
        }

        public String findByMemberidAndTypeAndId(@Param("userid")int userid, @Param("type")int type, @Param("id")int id) {
           String sql = "SELECT * FROM lm_member_fav WHERE member_id = #{userid} and state = 0";
           if(type==0){
               sql+=" and goods_id = #{id}";
           }else{
               sql+=" and video_id = #{id}";
           }
            return sql;
        }


    }
}
