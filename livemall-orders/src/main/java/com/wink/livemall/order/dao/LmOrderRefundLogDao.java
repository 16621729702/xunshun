package com.wink.livemall.order.dao;

import com.wink.livemall.order.dao.LmMerchOrderDao.sqlprovider;
import com.wink.livemall.order.dto.LmExpress;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderRefundLog;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface LmOrderRefundLogDao extends tk.mybatis.mapper.common.Mapper<LmOrderRefundLog> {


	
	 @SelectProvider(type =sqlprovider.class,method = "updateByFields" )
		void updateByFields(Map<String, String> params,String id);

	 @Select("SELECT * FROM lm_order_refund_log WHERE orderid = #{orderid} and status>=0 limit 1")
	    LmOrderRefundLog findByorderid(@Param("orderid")String orderid);



    class sqlprovider{
		 public String updateByFields(List<Map<String, String>> params,String id) {
	        	StringBuilder builder=new StringBuilder();
	        	builder.append("update lm_goods set ");
	        	for (int i = 0; i < params.size(); i++) {
					Map<String, String> m=params.get(i);
					String field=m.get("field");
					String value=m.get("value");
					if(i==params.size()-1) {
						builder.append(""+field+"='"+value+"'");
					}else {
						builder.append(""+field+"='"+value+"',");
					}
				}
	        	
	        	builder.append(" where id="+id);
	        	return builder.toString();
	        }
		 
	 }

}
