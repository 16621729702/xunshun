package com.wink.livemall.coupon.dao;

import com.wink.livemall.coupon.dto.LmCoupons;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmCouponsDao extends tk.mybatis.mapper.common.Mapper<LmCoupons>{

    @SelectProvider(type = LmCouponsDaoprovider.class, method = "findByCondient")
    List<Map<String, Object>> findByCondient(Map<String, Object> condient);

    class LmCouponsDaoprovider{

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
