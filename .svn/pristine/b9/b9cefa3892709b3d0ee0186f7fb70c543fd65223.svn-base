package com.wink.livemall.order.dao;

import com.wink.livemall.order.dto.LmOrderExpress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.Map;

@Mapper
public interface LmOrderExpressDao extends tk.mybatis.mapper.common.Mapper<LmOrderExpress>{

    @SelectProvider(type = LmOrderExpressDaoprovider.class, method = "findInfoById")
    Map<String,Object> findByOrderid(String orderid);

    class LmOrderExpressDaoprovider{

        public String findInfoById(String orderid) {
            String sql = "SELECT " +
                    " e.id as id, " +
                    " e.orderid as orderid, " +
                    " e.sendphone as sendphone, " +
                    " e.sendname as sendname, " +
                    " e.expressorderid as expressorderid, " +
                    " e.deliverytype as deliverytype, " +
                    " e.expressid as expressid, " +
                    " ex.name as expressname " +
                    " from lm_order_express e left join lm_setting_expresses ex on e.expressid = ex.id " +
                    " where e.orderid = '"+orderid+"'";
            return sql;
        }
    }
}
