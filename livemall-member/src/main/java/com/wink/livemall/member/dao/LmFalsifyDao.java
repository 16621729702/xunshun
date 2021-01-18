package com.wink.livemall.member.dao;

import com.wink.livemall.member.dto.LmFalsify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmFalsifyDao extends tk.mybatis.mapper.common.Mapper<LmFalsify> {

    @SelectProvider(type = LmFalsifyDaosprovider.class, method = "getfindfalsify")
    List<Map<String, Object>> findFalsify(@Param("memberid")int memberid,@Param("status")int status);


    @Select("SELECT * FROM lm_falsify where falsify_id= #{falsifyId} ")
    LmFalsify findFalsifyId(@Param("falsifyId")String falsifyId);

    @Select("SELECT * FROM lm_falsify where type=0 and member_id= #{memberid} and goodstype =#{goodstype} and good_id =#{goodid}")
    LmFalsify isFalsify(@Param("memberid")int memberid,@Param("goodid")int goodid,@Param("goodstype")int goodstype);

    class LmFalsifyDaosprovider {

        public String getfindfalsify(@Param("memberid")int memberid,@Param("status")int status) {

            String sql ="SELECT lf.*," +
                    "lmi.store_name as merchname FROM lm_falsify lf" +
                    "  LEFT JOIN lm_merch_info lmi on lf.merch_id " +
                    "where lf.member_id=  #{memberid}" ;
                    if(0==status){
                        sql += " and lf.type= 0 and lf.status =#{status} ";
                    }else if(1==status){
                        sql += " and lf.type= 0 and lf.status =#{status} ";
                    }else if(2==status){
                        sql += " and lf.type= 0 and lf.status =#{status} ";
                    } else if(3==status){
                        sql += " and lf.type= 1 and lf.status =#{status} ";
                    }
                sql +=" order by lf.id desc ";
            return sql;
        }
    }

}
