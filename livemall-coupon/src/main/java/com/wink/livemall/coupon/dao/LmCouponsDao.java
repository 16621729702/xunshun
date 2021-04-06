package com.wink.livemall.coupon.dao;

import com.wink.livemall.coupon.dto.LmCoupons;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface LmCouponsDao extends tk.mybatis.mapper.common.Mapper<LmCoupons>{

    @SelectProvider(type = LmCouponsDaoprovider.class, method = "findByCondient")
    List<Map<String, Object>> findByCondient(Map<String, Object> condient);

    @Select("SELECT id, seller_id as sellerId," +
            " coupon_name as couponName, coupon_value as couponValue, " +
            " min_amount as minAmount, send_start_time as sendStartTime," +
            " send_end_time as sendEndTime, person_limit_num as personLimitNum, " +
            " use_start_time as useStartTime, use_end_time as useEndTime, product_type as productType," +
            " create_time as createTime, received_num as receivedNum, update_time as updateTime , " +
            " total_limit_num as totalLimitNum , `status`,remark ,goods_type as goodsType,coupon_type as couponType, " +
            " type , product_ids as productIds  " +
            " FROM lm_coupons " +
            " WHERE seller_id =  #{merchId} AND `status` = #{status} AND type = 2 "+
            " ORDER BY min_amount DESC" )
    List<LmCoupons> findMerchCouponByMId(@Param("merchId") Integer merchId,@Param("status") Integer status);

    @SelectProvider(type = LmCouponsDaoprovider.class, method = "findMemberCouponByMId")
    List<Map<String, Object>> findMemberCouponByMId(@Param("memberId")Integer memberId, @Param("merchId")Integer merchId);

    @Select("SELECT id, seller_id as sellerId," +
            " coupon_name as couponName, coupon_value as couponValue, " +
            " min_amount as minAmount, send_start_time as sendStartTime," +
            " send_end_time as sendEndTime, person_limit_num as personLimitNum, " +
            " use_start_time as useStartTime, use_end_time as useEndTime,type," +
            " total_limit_num as totalLimitNum , `status`,remark FROM lm_coupons " +
            " WHERE type =#{type} ORDER BY create_time " )
    List<LmCoupons> findNewCouponByMId(@Param("type") Integer type);

    @Select("SELECT id, seller_id as sellerId," +
            " coupon_name as couponName, coupon_value as couponValue, " +
            " min_amount as minAmount, send_start_time as sendStartTime," +
            " send_end_time as sendEndTime, person_limit_num as personLimitNum, " +
            " use_start_time as useStartTime, use_end_time as useEndTime,type," +
            " total_limit_num as totalLimitNum , `status`,remark FROM lm_coupons " +
            " WHERE `status` = 0 AND type = 0 ORDER BY create_time " )
    List<LmCoupons> findNewCouponList();


    @Select("SELECT id, seller_id as sellerId," +
            " coupon_name as couponName, coupon_value as couponValue, " +
            " min_amount as minAmount, send_start_time as sendStartTime," +
            " send_end_time as sendEndTime, person_limit_num as personLimitNum, " +
            " use_start_time as useStartTime, use_end_time as useEndTime,type," +
            " total_limit_num as totalLimitNum , `status`,remark FROM lm_coupons " +
            " WHERE `status` = 0 AND type = 2 ORDER BY create_time DESC" )
    List<LmCoupons> findMerchCouponBys();

    @Select("SELECT lcm.*,lc.coupon_value as couponValue," +
            " lc.min_amount as minAmount,lc.coupon_name as couponName, " +
            " lc.use_start_time as useStartTime," +
            " lc.use_end_time as useEndTime," +
            " lc.goods_type as goodsType," +
            " lc.product_type as productType," +
            " lc.type, lc.coupon_type as couponType " +
            " FROM lm_coupon_member lcm LEFT JOIN lm_coupons lc ON lcm.coupon_id=lc.id " +
            " WHERE  lcm.can_use =1 AND lc.`status` =0  AND lcm.member_id = #{memberId}" +
            " ORDER BY lc.coupon_value DESC" )
    List<Map<String,Object>> orderCouponList(@Param("memberId") Integer memberId);

    class LmCouponsDaoprovider{

        public String findMemberCouponByMId(@Param("memberId")Integer memberId, @Param("merchId")Integer merchId) {
            String sql = "SELECT lcm.*," +
                    " lc.coupon_name as couponName," +
                    " lc.min_amount as minAmount," +
                    " lc.coupon_value as couponValue," +
                    " lc.use_start_time as useStartTime," +
                    " lc.use_end_time as useEndTime," +
                    " lc.goods_type as goodsType," +
                    " lc.product_type as productType," +
                    " lc.coupon_type as couponType," +
                    " lc.type as type,lc.`status`,lc.remark" +
                    " FROM lm_coupon_member lcm LEFT JOIN lm_coupons lc on lcm.coupon_id = lc.id " +
                    " where lcm.member_id = #{memberId} "+
                    " ORDER BY lcm.receive_time DESC" ;
            return sql;
        }

        public String findByCondient(Map<String, Object> condient) {
            String sql = "SELECT useprice as useprice,id as id,merch_id as merch_id,description as description,rate as rate from lm_coupons  where left_num > 0 ";
            if(condient.get("merchid")!=null){
                sql += " and merch_id = "+condient.get("merchid");
            }
            if(condient.get("datetime")!=null){
                sql += " and start_date <= '"+condient.get("datetime")+"'";
            }
            if(condient.get("datetime")!=null){
                sql += " and end_date >= '"+condient.get("datetime")+"'";
            }
            sql += " order by useprice desc ";
            return sql;
        }

    }



}
