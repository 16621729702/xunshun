package com.wink.livemall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.wink.livemall.goods.dao.GoodDao;
import com.wink.livemall.goods.dao.LiveGoodDao;
import com.wink.livemall.goods.dao.LmGoodAuctionDao;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.member.dao.LmFalsifyDao;
import com.wink.livemall.member.dto.LmFalsify;
import com.wink.livemall.member.service.LmFalsifyService;
import com.wink.livemall.order.dao.LmOrderGoodsDao;
import com.wink.livemall.order.dao.LmShopOrderDao;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.dto.LmPayLog;
import com.wink.livemall.sys.setting.dto.Configs;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class LmFalsifyServiceImpl implements LmFalsifyService {


    @Resource
    private LmFalsifyDao lmFalsifyDao;

    @Resource
    private GoodDao goodDao;

    @Resource
    private LmGoodAuctionDao lmGoodAuctionDao;
    @Resource
    private LmOrderGoodsDao lmOrderGoodsDao;
    @Resource
    private LmShopOrderDao lmShopOrderDao;

    @Resource
    private LiveGoodDao liveGoodDao;

    @Override
    public LmFalsify findById(String id) {
        return lmFalsifyDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void insertService(LmFalsify lmFalsify) {
        lmFalsifyDao.insertSelective(lmFalsify);
    }

    @Override
    public void updateService(LmFalsify lmFalsify) {
        lmFalsifyDao.updateByPrimaryKeySelective(lmFalsify);
    }

    @Override
    public void deleteService(int id) {
        lmFalsifyDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<Map<String, Object>> getMerchFalsifyList(String merchId,String status) {
        return lmFalsifyDao.getMerchFalsifyList(Integer.parseInt(merchId),Integer.parseInt(status));
    }

    @Override
    public List<Map<String, Object>> findFalsify(String memberid, String status) {
        return lmFalsifyDao.findFalsify(Integer.parseInt(memberid),Integer.parseInt(status));
    }

    @Override
    public LmFalsify isFalsify(String memberid, String goodid, String goodstype) {
        return lmFalsifyDao.isFalsify(Integer.parseInt(memberid),Integer.parseInt(goodid),Integer.parseInt(goodstype));
    }

    @Override
    public LmFalsify findFalsifyId(String falsifyId) {
        return lmFalsifyDao.findFalsifyId(falsifyId);
    }

    @Override
    public List<LmFalsify> findNoFalsify() {
        return lmFalsifyDao.findNoFalsify();
    }

    @Override
    public BigDecimal falsifySum(int merId) {
        List<LmFalsify> lmFalsifies = lmFalsifyDao.findFalsifySum(merId, 3);
        BigDecimal sum=new BigDecimal(0);
        if(lmFalsifies.size()>0){
            for(LmFalsify lmFalsify:lmFalsifies){
                BigDecimal realPrice = lmFalsify.getFalsify();
                if(lmFalsify.getPaystatus()==3){
                    realPrice = realPrice.multiply(new BigDecimal(94.4)).divide(new BigDecimal(100));
                }else {
                    realPrice = realPrice.multiply(new BigDecimal(94.7)).divide(new BigDecimal(100));
                }
                sum=sum.add(realPrice);
            }
        }
        List<LmFalsify> falsifySum = lmFalsifyDao.findFalsifySum(merId, 4);
        if(falsifySum.size()>0){
            for(LmFalsify lmFalsify:falsifySum){
                BigDecimal realPrice = lmFalsify.getFalsify();
                if(lmFalsify.getPaystatus()==3){
                    realPrice = realPrice.multiply(new BigDecimal(94.4)).divide(new BigDecimal(100));
                }else {
                    realPrice = realPrice.multiply(new BigDecimal(94.7)).divide(new BigDecimal(100));
                }
                sum=sum.add(realPrice);
            }
        }
        sum=sum.setScale(2,BigDecimal.ROUND_DOWN);
        return sum;
    }

    @Override
    public List<Map<String, Object>> merFalsifyList(Integer merId) {
        List<Map<String, Object>> List=new LinkedList<>();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<LmFalsify> lmFalsifies = lmFalsifyDao.findFalsifySum(merId, 3);
        if(lmFalsifies.size()>0){
            for(LmFalsify lmFalsify:lmFalsifies){
                Map<String,Object> map=new HashMap<>();
                if(lmFalsify.getGoodstype()==0){
                    Good byId = goodDao.findById(lmFalsify.getGood_id());
                    map.put("description","因"+byId.getTitle()+"缴纳的违约金");
                }else if(lmFalsify.getGoodstype()==1){
                    LivedGood byId = liveGoodDao.findById(lmFalsify.getGood_id());
                    map.put("description","因"+byId.getName()+"缴纳的违约金");
                }
                BigDecimal realPrice = lmFalsify.getFalsify();
                if(lmFalsify.getPaystatus()==3){
                    realPrice = realPrice.multiply(new BigDecimal(94.4)).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
                }else {
                    realPrice = realPrice.multiply(new BigDecimal(94.7)).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
                }
                map.put("price",realPrice);
                map.put("time",dateFormat.format(lmFalsify.getCreate_time()));
                map.put("type",0);
                List.add(map);
            }
        }
        List<LmFalsify> falsifySum = lmFalsifyDao.findFalsifySum(merId, 4);
        if(falsifySum.size()>0){
            for(LmFalsify lmFalsify:falsifySum){
                Map<String,Object> map=new HashMap<>();
                if(lmFalsify.getGoodstype()==0){
                    Good byId = goodDao.findById(lmFalsify.getGood_id());
                    map.put("description","因"+byId.getTitle()+"缴纳的违约金");
                }else if(lmFalsify.getGoodstype()==1){
                    LivedGood byId = liveGoodDao.findById(lmFalsify.getGood_id());
                    map.put("description","因"+byId.getName()+"缴纳的违约金");
                }
                BigDecimal realPrice = lmFalsify.getFalsify();
                if(lmFalsify.getPaystatus()==3){
                    realPrice = realPrice.multiply(new BigDecimal(94.4)).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
                }else {
                    realPrice = realPrice.multiply(new BigDecimal(94.7)).divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN);
                }
                map.put("price",realPrice);
                map.put("time",dateFormat.format(lmFalsify.getCreate_time()));
                map.put("type",0);
                List.add(map);
            }
        }
        return List;
    }


    @Override
    public Map<String, String> isRefundFalsify(String falsifyId) {
        LmFalsify lmFalsify = lmFalsifyDao.findFalsifyId(falsifyId);
        System.out.println("请求参数对象违约金  +++++++："+lmFalsify);
        Map<String,String> _resultMap = new HashMap<String,String>();
        if(0==lmFalsify.getType()){
            if(0==lmFalsify.getStatus()){
                if(0==lmFalsify.getGoodstype()){
                    //普通商品
                    Good good = goodDao.findById(lmFalsify.getGood_id());
                    System.out.println("请求参数对象普通商品："+good);
                    if(0==good.getState()){
                        LmGoodAuction lmGoodAuction = lmGoodAuctionDao.findnowPriceByGoodidByApi(lmFalsify.getGood_id(), lmFalsify.getGoodstype());
                        if(null!=lmGoodAuction){
                          if(("2").equals(lmGoodAuction.getStatus())){
                            if(lmGoodAuction.getMemberid()==lmFalsify.getMember_id()){
                                LmOrderGoods lmOrderGoods = lmOrderGoodsDao.findByGoodsid0(lmFalsify.getGood_id());
                                LmOrder lmOrder = lmShopOrderDao.selectByPrimaryKey(lmOrderGoods.getOrderid());
                                if(("0").equals(lmOrder.getStatus())){
                                    _resultMap.put("errCode","FAIL");
                                    _resultMap.put("msg","你已拍中还未付款");
                                    return _resultMap;
                                }else if(("-1").equals(lmOrder.getStatus())){
                                    _resultMap.put("errCode","FAIL");
                                    _resultMap.put("msg","你已拍中但未进行付款操作，已违约");
                                    return _resultMap;
                                }else if(("1").equals(lmOrder.getStatus())){
                                    _resultMap.put("errCode","FAIL");
                                    _resultMap.put("msg","交易未完成，违约金暂时无法退还");
                                    return _resultMap;
                                }else if(("2").equals(lmOrder.getStatus())){
                                    _resultMap.put("errCode","FAIL");
                                    _resultMap.put("msg","交易未完成，违约金暂时无法退还，请确认收货");
                                    return _resultMap;
                                }
                                if(1==lmOrder.getBackstatus()){
                                    _resultMap.put("errCode","FAIL");
                                    _resultMap.put("msg","您已申请退款，违约金不可退还");
                                    return _resultMap;
                                }else if(2==lmOrder.getBackstatus()){
                                    _resultMap.put("errCode","FAIL");
                                    _resultMap.put("msg","退款完成，违约金不可退还");
                                    return _resultMap;
                                }
                            }
                          }
                        }
                    }else {
                        _resultMap.put("errCode","FAIL");
                        _resultMap.put("msg","商品还在拍卖中");
                        return _resultMap;
                    }
                }else {
                    //直播商品
                    LivedGood good = liveGoodDao.selectByPrimaryKey(lmFalsify.getGood_id());
                    System.out.println("请求参数对象直播商品："+good);
                    if(0!=good.getStatus()){
                        LmGoodAuction liveGoodAuction = lmGoodAuctionDao.findnowPriceByGoodidByApi(lmFalsify.getGood_id(), lmFalsify.getGoodstype());
                        if(null!=liveGoodAuction){
                          if(("2").equals(liveGoodAuction.getStatus())){
                            if(liveGoodAuction.getMemberid()==lmFalsify.getMember_id()){
                                LmOrderGoods lmOrderGoods = lmOrderGoodsDao.findByGoodsid1(lmFalsify.getGood_id());
                                LmOrder lmLiveOrder = lmShopOrderDao.selectByPrimaryKey(lmOrderGoods.getOrderid());
                                if(("0").equals(lmLiveOrder.getStatus())){
                                    _resultMap.put("errCode","FAIL");
                                    _resultMap.put("msg","你已拍中还未付款");
                                    return _resultMap;
                                }else if(("-1").equals(lmLiveOrder.getStatus())){
                                    _resultMap.put("errCode","FAIL");
                                    _resultMap.put("msg","你已拍中但未进行付款操作，已违约");
                                    return _resultMap;
                                }else if(("1").equals(lmLiveOrder.getStatus())){
                                    _resultMap.put("errCode","FAIL");
                                    _resultMap.put("msg","交易未完成，违约金暂时无法退还");
                                    return _resultMap;
                                }else if(("2").equals(lmLiveOrder.getStatus())){
                                    _resultMap.put("errCode","FAIL");
                                    _resultMap.put("msg","交易未完成，违约金暂时无法退还，请确认收货");
                                    return _resultMap;
                                }
                                if(1==lmLiveOrder.getBackstatus()){
                                    _resultMap.put("errCode","FAIL");
                                    _resultMap.put("msg","拍中的商品您已申请退款，违约金不可退还");
                                    return _resultMap;
                                }else if(2==lmLiveOrder.getBackstatus()){
                                    _resultMap.put("errCode","FAIL");
                                    _resultMap.put("msg","你申请的商品退款已完成，违约金不可退还");
                                    return _resultMap;
                                }
                            }
                        }
                        }
                    }else {
                        _resultMap.put("errCode","FAIL");
                        _resultMap.put("msg","商品还在拍卖中");
                        return _resultMap;
                    }
                }
            }else if(1==lmFalsify.getStatus()){
                _resultMap.put("errCode","FAIL");
                _resultMap.put("msg","正在审核中！");
                return _resultMap;
            }else if (2==lmFalsify.getStatus()){
                _resultMap.put("errCode","FAIL");
                _resultMap.put("msg","退款已完成！");
                return _resultMap;
            }else if (3==lmFalsify.getStatus()){
                _resultMap.put("errCode","FAIL");
                _resultMap.put("msg","用户已违约！");
                return _resultMap;
            }
        }else {
            _resultMap.put("errCode","FAIL");
            _resultMap.put("msg","用户已违约！");
            return _resultMap;
        }

        return _resultMap;
    }

    @Override
    public List<Map<String,Object>>  autoRefundFalsify(int goodId, int goodsType) {
        return lmFalsifyDao.autoRefundFalsify(goodId, goodsType);
    }


}
