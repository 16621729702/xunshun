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


    @SelectProvider(type = LmFalsifyDaosprovider.class, method = "getMerchFalsifyList")
    List<Map<String, Object>> getMerchFalsifyList(@Param("merchId")int merchId,@Param("status")int status);

    @Select("SELECT * FROM lm_falsify where falsify_id= #{falsifyId} ORDER BY create_time limit 1")
    LmFalsify findFalsifyId(@Param("falsifyId")String falsifyId);

    @Select("SELECT * FROM lm_falsify where type=0 and status=0 and member_id= #{memberid} and goodstype =#{goodstype} and good_id =#{goodid} ORDER BY create_time limit 1")
    LmFalsify isFalsify(@Param("memberid")int memberid,@Param("goodid")int goodid,@Param("goodstype")int goodstype);

    @Select("SELECT * FROM lm_falsify where type=0 and  goodstype =#{goodsType} and good_id =#{goodId} ")
    List<Map<String,Object>>  autoRefundFalsify(@Param("goodId")int goodId,@Param("goodsType")int goodsType);

    @Select("SELECT * FROM lm_falsify where type = 1 and status = 1 and violate = 0 ")
    List<LmFalsify>  findNoFalsify();

    @Select("SELECT * FROM lm_falsify where type = 1 and merch_id = #{merId} and status = #{status}  ORDER BY create_time desc ")
    List<LmFalsify>  findFalsifySum(@Param("merId")int merId,@Param("status")int status);


    class LmFalsifyDaosprovider {

        public String getfindfalsify(@Param("memberid")int memberid,@Param("status")int status) {

            String sql ="SELECT lf.*," +
                    " lmi.store_name as merchName FROM lm_falsify lf" +
                    " LEFT JOIN lm_merch_info lmi on lf.merch_id = lmi.id " +
                    " where lf.member_id=  #{memberid} " ;
                    if(0==status){
                        sql += " and lf.type= 0 and lf.status =#{status} ";
                    }else if(1==status){
                        sql += " and lf.type= 1 and lf.status =#{status} ";
                    }else if(2==status){
                        sql += " and lf.status =#{status} ";
                    }else if(3==status){
                        sql += " and lf.type= 1 and lf.status =#{status} ";
                    }else if(4==status){
                        sql += " and lf.type= 1 and lf.status =#{status} ";
                    }
                sql +=" order by lf.id desc ";
            return sql;
        }

        public String getMerchFalsifyList(@Param("merchId")int merchId,@Param("status")int status) {

            String sql ="SELECT lf.*," +
                    " lmi.store_name as merchName FROM lm_falsify lf" +
                    " LEFT JOIN lm_merch_info lmi on lf.merch_id = lmi.id " +
                    " where lf.merch_id=  #{merchId} " ;
            if(0==status){
                sql += " and lf.type= 1 and lf.status =1 ";
            }else if(1==status){
                sql += " and lf.type= 1 and lf.status =3 ";
            }else if(2==status){
                sql += " and lf.type= 1 and lf.status =2 ";
            }else if(3==status){
                sql += " and lf.type= 1 and lf.status =4 ";
            }
            sql +=" order by lf.id desc ";
            return sql;
        }
    }

}
