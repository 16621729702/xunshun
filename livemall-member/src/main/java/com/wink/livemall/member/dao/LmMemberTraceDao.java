package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.LmMemberCoupon;
import com.wink.livemall.member.dto.LmMemberTrace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmMemberTraceDao  extends tk.mybatis.mapper.common.Mapper<LmMemberTrace> {

    @Select("SELECT * FROM lm_member_trace WHERE member_id = #{memberid}")
    List<LmMemberTrace> findByMemberid(@Param("memberid")int memberid);

    @SelectProvider(type = LmMemberTraceDaoprovider.class, method = "findByMemberidAndTypeLeftGood")
    List<Map<String, Object>> findByMemberidAndTypeLeftGood(@Param("userid")int userid,@Param("type") int type);

    @SelectProvider(type = LmMemberTraceDaoprovider.class, method = "findByMemberidAndTypeLeftLive")
    List<Map<String, Object>> findByMemberidAndTypeLeftLive(@Param("userid")int userid,@Param("type") int type);

    @Select("SELECT * FROM lm_member_trace WHERE member_id = #{memberid} and trace_id = #{goodid} and trace_type = 2")
    LmMemberTrace findByMemberidAndGoodid(@Param("memberid")int userid, @Param("goodid")int goodid);

    @Select("SELECT * FROM lm_member_trace WHERE member_id = #{memberid} and trace_id = #{liveid} and trace_type = 1")
    LmMemberTrace findByMemberidAndLiveid(@Param("memberid")int userid, @Param("liveid")int liveid);

    class LmMemberTraceDaoprovider{

        public String findByMemberidAndTypeLeftGood(@Param("userid")int userid, @Param("type")int type) {
            String sql = "SELECT " +
                    " lg.title as goodname," +
                    " lg.thumb as img," +
                    " lg.id as id," +
                    " lg.type as type," +
                    " lg.bidsnum as bidsnum," +
                    " lg.mer_id as merId," +
                    " lg.productprice as price" +
                    " from lm_goods lg  left join lm_member_trace lmt   on lmt.trace_id = lg.id where " +
                    " lmt.member_id = #{userid}";
            sql+=" and lmt.trace_type = #{type}";
            sql+=" order by trace_time desc ";
            return sql;
        }
        public String findByMemberidAndTypeLeftLive(@Param("userid")int userid,@Param("type") int type) {
            String sql = "SELECT " +
                    " ll.name as livename," +
                    " ll.id as id, " +
                    " ll.type as type, " +
                    " ll.img as liveimg, " +
                    " ll.isstart as isstart, " +
                    " ll.pushurl as pushurl, " +
                    " lm.store_name as store_name," +
                    " lm.avatar as avatar," +
                    " ll.watchnum as watchnum, " +
                    " ll.preview_time as preview_time " +
                    " from lm_member_trace lmt,lm_lives ll,lm_merch_info lm" +
                    " where lmt.trace_id = ll.id and ll.merch_id = lm.id " +
                    " and lmt.member_id = #{userid}";
            sql+=" and lmt.trace_type = #{type}";
            sql+=" order by trace_time desc ";
            return sql;
        }
    }
}
