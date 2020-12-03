package com.wink.livemall.order.service;

import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderRefundLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LmOrderService {
    //普通订单
    List<Map<String, Object>> findByCondient(Map<String, String> condient);
    //合买订单
    List<Map<String, Object>> findByCondient2(Map<String, String> condient);

    LmOrder findById(String id);

    void updateService(LmOrder lmOrder);

    Map<String, Object> findInfoById(String id);

    void delete(String id);

    LmOrder findByOrderId(String orderid);

    List<Map<String, Object>> findListByTypeByApi(String status,int userid);

    List<Map<String, String>> findListByMerchidByApi(int merchid);

    List<Map<String, String>> findCommentinfiByMerchidByApi(int merchid);

    void insertService(LmOrder lmOrder);

    Integer findMaxId();

    //订单长度
    Integer ordersize(int status,int userid);

    LmOrder findTopOrder(int goodid, int merchid);

    void insertRefindLogService(LmOrderRefundLog lmOrderRefundLog);

    void updateRefundLogService(LmOrderRefundLog lmOrderRefundLog);

    LmOrderRefundLog findRefundLogById(int refundid);

    /**
     * 查询已付款的子订单
     * @param id
     * @return
     */
    List<LmOrder> findOrderListByPid(int id);

    List<Map<String, Object>> findMyshare(int status, int userid);

    List<Map<String, Object>> findSharelist(int status, int liveid,int userid);

    List<Map<String, Object>> findMyshare2(int status, int merchid);

    List<LmOrder> findListByDate(Date olddate);

    List<Map<String, Object>> findMerchshareList(String merchid, String type);

    List<Map<String, Object>> findOrderList(String status, int parseInt);

    List<LmOrder> findShareOrderListByMerchid(int merchid);

    List<LmOrder> findTopOrderList();
    /**
     * 查询所有子订单
     * @param id
     * @return
     */
    List<LmOrder> findChildOrder(int id);


    void deleteByOrderno(LmOrder order);

    List<LmOrder> findOrderListByPid2(int id);

    List<LmOrder> findOrderListByStatus(int i);

    LmOrder findByNewOrderid(String out_trade_no);
    
    
}
