package com.wink.livemall.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.wink.livemall.order.dao.LmMerchOrderDao;
import com.wink.livemall.order.dao.LmOrderRefundLogDao;
import com.wink.livemall.order.dao.LmPayLogDao;
import com.wink.livemall.order.dao.LmShopOrderDao;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderRefundLog;
import com.wink.livemall.order.dto.LmPayLog;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;

import net.sf.json.JSONObject;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LmShopOrderServiceImpl implements LmOrderService {
    @Resource
    private LmOrderRefundLogDao lmOrderRefundLogDao;
    @Resource
    private LmShopOrderDao lmShopOrderDao;
    
    
    @Resource
    private LmPayLogDao lmPayLogDao;
    @Autowired
    private ConfigsService configsService;
    
    @Override
    public List<Map<String, Object>> findByCondient(Map<String, String> condient) {
        return lmShopOrderDao.findByCondient(condient);
    }

    @Override
    public List<Map<String, Object>> findByCondient2(Map<String, String> condient) {
        return lmShopOrderDao.findByCondient2(condient);
    }

    @Override
    public LmOrder findById(String id) {
        return lmShopOrderDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void updateService(LmOrder lmOrder) {
        lmShopOrderDao.updateByPrimaryKey(lmOrder);
    }

    @Override
    public Map<String, Object> findInfoById(String id) {
        return lmShopOrderDao.findInfoById(Integer.parseInt(id));
    }

    @Override
    public void delete(String id) {
        lmShopOrderDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public LmOrder findByOrderId(String orderid) {
        return lmShopOrderDao.findByOrderId(orderid);
    }

    @Override
    public List<Map<String, Object>> findListByTypeByApi(String status,int userid) {
        return lmShopOrderDao.findListByTypeByApi(status,userid);
    }

    @Override
    public List<Map<String, String>> findListByMerchidByApi(int merchid) {
        return lmShopOrderDao.findListByMerchidByApi(merchid);
    }

    @Override
    public List<Map<String, String>> findCommentinfiByMerchidByApi(int merchid) {
        return lmShopOrderDao.findCommentinfiByMerchidByApi(merchid);
    }

    @Override
    public void insertService(LmOrder lmOrder) {
        lmShopOrderDao.insert(lmOrder);
    }

    @Override
    public Integer findMaxId() {
        return lmShopOrderDao.findMaxId();
    }

    @Override
    public Integer ordersize(int status,int userid) {
        return lmShopOrderDao.ordersize(status,userid);
    }

    @Override
    public LmOrder findTopOrder(int goodid, int merchid) {
        return lmShopOrderDao.findTopOrder(goodid,merchid);
    }

    @Override
    public void insertRefindLogService(LmOrderRefundLog lmOrderRefundLog) {
        lmOrderRefundLogDao.insert(lmOrderRefundLog);
    }

    @Override
    public void updateRefundLogService(LmOrderRefundLog lmOrderRefundLog) {
        lmOrderRefundLogDao.updateByPrimaryKey(lmOrderRefundLog);
    }

    @Override
    public LmOrderRefundLog findRefundLogById(int refundid) {
        return lmOrderRefundLogDao.selectByPrimaryKey(refundid);
    }

    @Override
    public List<LmOrder> findOrderListByPid(int id) {
        return lmShopOrderDao.findOrderListByPid(id);
    }

    @Override
    public List<Map<String, Object>> findMyshare(int status, int userid) {
        return lmShopOrderDao.findMyshare(status,userid);
    }

    @Override
    public List<Map<String, Object>> findSharelist(int status, int liveid,int userid) {
        return lmShopOrderDao.findSharelist(status,liveid,userid);
    }

    @Override
    public List<Map<String, Object>> findMyshare2(int status, int merchid) {
        return lmShopOrderDao.findMyshare2(status, merchid);
    }

    @Override
    public List<LmOrder> findListByDate(Date olddate) {
        return lmShopOrderDao.findListByDate(olddate);
    }

    @Override
    public List<Map<String, Object>> findMerchshareList(String merchid, String type) {
        return lmShopOrderDao.findMerchshareList(Integer.parseInt(merchid),type);
    }

    @Override
    public List<Map<String, Object>> findOrderList(String status, int userid) {
        return lmShopOrderDao.findOrderList(status,userid);
    }

    @Override
    public List<LmOrder> findShareOrderListByMerchid(int merchid) {
        return lmShopOrderDao.findShareOrderListByMerchid(merchid);
    }

    @Override
    public List<LmOrder> findTopOrderList() {
        return lmShopOrderDao.findTopOrderList();
    }

    @Override
    public List<LmOrder> findChildOrder(int id) {
        return lmShopOrderDao.findChildOrder(id);
    }

    @Override
    public void deleteByOrderno(LmOrder order) {
        //删除订单号
        lmShopOrderDao.delete(order);
    }

    @Override
    public List<LmOrder> findOrderListByPid2(int id) {
        return lmShopOrderDao.findOrderListByPid2(id);
    }

    @Override
    public List<LmOrder> findOrderListByStatus(int status) {
        return lmShopOrderDao.findOrderListByStatus(status);
    }

    @Override
    public LmOrder findByNewOrderid(String out_trade_no) {
        return lmShopOrderDao.findByNewOrderid(out_trade_no);
    }

 


}
