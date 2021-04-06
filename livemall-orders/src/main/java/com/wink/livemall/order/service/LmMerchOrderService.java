package com.wink.livemall.order.service;

import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderRefundLog;

import java.text.ParseException;
import java.util.List;
import java.util.Map;import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public interface LmMerchOrderService {
	
	Map<String, Object> findChippedByGoodsId(String goodsid);
	
	/**
	 * 获取到的列表 
	 * @param porderid
	 * @param order 1 按时间倒叙排列  2按签号顺序排列
	 * @return
	 */
	List<Map<String, Object>> findChippedOrder(String porderid,String order);

    List<LmOrder> findChippedOrder2(int porderid);


    List<Map<String, Object>> findListByCondient(Map<String, String> condient);
    
    List<Map<String, Object>> findPage(Map<String, String> parmas);
    
    List<Map<String, Object>> findChildrenOrder(Map<String, String> params);
    
    void updateByFields(List<Map<String, String>> params,String id);
    
    boolean checkExist(String id);
    
    void addRefundLog(LmOrderRefundLog entity);
    
    /**
     * 成拍
     * @param merchid
     * @return
     */
    Map<String, Object> staticOrderOk(String merchid);
    
    List<Map<String, Object>> orderOkList(Map<String, String> parmas);
    
    /**
     * 销售 付款
     * @param merchid
     * @return
     */
    Map<String, Object> staticOrderPay(String merchid);
    List<Map<String, Object>> orderPayList(Map<String, String> parmas);
    
    /**
     * 销售 退款
     * @param merchid
     * @return
     */
    Map<String, Object> staticOrderRefund(String merchid,int backStatus);
    List<Map<String, Object>> orderRefundList(Map<String, String> parmas);
    /**
     * 收款
     * @param merchid
     * @return
     */
    Map<String, Object> staticOrderEarn(String merchid,int status);
    List<Map<String, Object>> orderEarnList(Map<String, String> parmas);
    
    /**
     * 直播订单
     * @param merchid
     * @return
     */
    Map<String, Object> staticLive(String merchid);
    int staticLiveNum(String merchid);
    List<Map<String, Object>> orderLiveList(Map<String, String> parmas);
    
    /**
     * 所有订单数量
     * @param merchid
     * @return
     */
    Map<String, Object> staticAll(String merchid);
 
    Map<String, Object> staticOrderMonth(String merchid,String type);
  
    LmOrderRefundLog getRefundLog(int id);
    LmOrderRefundLog getRefundLogByorderid(String id);
    
    void updRefundLog(LmOrderRefundLog log);
    
    //待发货
    int countPay(String merchid);
    //退款处理
    int countRefund(String merchid);

    //列表记录
    Map<String, Object> orderLogList(String merId, Integer type, String startTime, String entTime) throws ParseException;

    //直播销售列表
    Map<String, Object> staticLiveList(String merId, String startTime, String entTime)throws ParseException;
    
}
