package com.wink.livemall.order.service.impl;

import com.wink.livemall.goods.dao.GoodDao;
import com.wink.livemall.goods.dao.LiveGoodDao;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.order.dao.*;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.dto.LmOrderRefundLog;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.sys.setting.service.ConfigsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LmShopOrderServiceImpl implements LmOrderService {
    @Resource
    private LmOrderRefundLogDao lmOrderRefundLogDao;
    @Resource
    private LmShopOrderDao lmShopOrderDao;
    @Resource
    private LmOrderGoodsDao lmOrderGoodsDao;
    @Resource
    private GoodDao goodDao;
    @Resource
    private LiveGoodDao liveGoodDao;

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
        lmShopOrderDao.updateByPrimaryKeySelective(lmOrder);
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
        lmShopOrderDao.insertSelective(lmOrder);
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
        lmOrderRefundLogDao.insertSelective(lmOrderRefundLog);
    }

    @Override
    public void updateRefundLogService(LmOrderRefundLog lmOrderRefundLog) {
        lmOrderRefundLogDao.updateByPrimaryKeySelective(lmOrderRefundLog);
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



    @Override
    public List<LmOrder> violateOrderOne(int type) {
        return lmShopOrderDao.violateOrderOne(type);
    }

    @Override
    public List<LmOrder> violateOrderTwo(int type) {
        return lmShopOrderDao.violateOrderTwo(type);
    }

    @Override
    public List<LmOrder> isBuy(int merId,int memberId) {
        return lmShopOrderDao.isBuy(merId,memberId);
    }

    @Override
    public BigDecimal merOrderPriceSum(Integer merId) {
        BigDecimal sum=new BigDecimal(0);
        List<LmOrder> lmOrders = lmShopOrderDao.merOrderPriceSum(merId);
        if(lmOrders.size()>0){
            for(LmOrder lmOrder:lmOrders){
                BigDecimal realPrice = lmOrder.getRealpayprice();
                if(lmOrder.getPaystatus()!=null){
                    if(lmOrder.getPaystatus().equals("3")){
                        realPrice = realPrice.multiply(new BigDecimal(94.4)).divide(new BigDecimal(100));
                    }else {
                        realPrice = realPrice.multiply(new BigDecimal(94.7)).divide(new BigDecimal(100));
                    }
                    sum=sum.add(realPrice);
                }
            }
        }
        sum=sum.setScale(2,BigDecimal.ROUND_DOWN);
        return sum;
    }

    @Override
    public List<Map<String, Object>> merOrderList(Integer merId) {
        List<Map<String, Object>> List=new LinkedList<>();
        List<LmOrder> lmOrders = lmShopOrderDao.merOrderPriceSum(merId);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(lmOrders.size()>0){
            for(LmOrder lmOrder:lmOrders){
                if(lmOrder.getPaystatus()!=null) {
                    Map<String, Object> map = new HashMap<>();
                    BigDecimal realPrice = lmOrder.getRealpayprice();
                    if (lmOrder.getPaystatus().equals("3")) {
                        realPrice = realPrice.multiply(new BigDecimal(94.4)).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN);
                    } else {
                        realPrice = realPrice.multiply(new BigDecimal(94.7)).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN);
                    }
                    LmOrderGoods byOrderid = lmOrderGoodsDao.findByOrderid(lmOrder.getId());
                    if (byOrderid.getGoodstype() == 0) {
                        Good byId = goodDao.findById(byOrderid.getGoodid());
                        map.put("description", byId.getTitle() + "确认收货");
                    } else if (byOrderid.getGoodstype() == 1) {
                        LivedGood byId = liveGoodDao.findById(byOrderid.getGoodid());
                        map.put("description", "直播商品" + byId.getName() + "确认收货");
                    }
                    map.put("price", realPrice);
                    map.put("time", dateFormat.format(lmOrder.getCreatetime()));
                    map.put("type", 0);
                    List.add(map);
                }
            }
        }

        return List;
    }

}
