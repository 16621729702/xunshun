package com.wink.livemall.coupon.service.impl;
import java.util.Date;

import com.wink.livemall.coupon.dao.LmCouponLogDao;
import com.wink.livemall.coupon.dao.LmCouponMemberDao;
import com.wink.livemall.coupon.dao.LmCouponsDao;
import com.wink.livemall.coupon.dto.LmConpouMember;
import com.wink.livemall.coupon.dto.LmCouponLog;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.dto.vo.LmCouponsVo;
import com.wink.livemall.coupon.service.LmCouponsService;
import com.wink.livemall.merch.dao.LmMerchInfoDao;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.utils.Errors;
import com.wink.livemall.utils.HttpJsonResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LmCouponsServiceImpl implements LmCouponsService {

    @Resource
    private LmCouponsDao lmCouponsDao;
    @Resource
    private LmCouponLogDao lmCouponLogDao;
    @Resource
    private LmMerchInfoDao lmMerchInfoDao;
    @Resource
    private LmCouponMemberDao lmCouponMemberDao;

    @Override
    public LmCoupons findById(String id) {
        return lmCouponsDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmCoupons> findMerchCoupon(Integer merchId,Integer status) {
        return  lmCouponsDao.findMerchCouponByMId(merchId, status);
    }


    @Override
    public List<LmCouponsVo> findMerchCouponUsable(Integer merchId,Integer status,String memberId) {
        List<LmCoupons> lmCoupons = lmCouponsDao.findMerchCouponByMId(merchId, status);
        List<LmCouponsVo> lmCouponsVos =new LinkedList<>();
        if(lmCoupons!=null&&lmCoupons.size()>0){
            for(LmCoupons lmCoupon:lmCoupons){
                LmCouponsVo lmCouponsVo=new LmCouponsVo();
                lmCouponsVo.setId(lmCoupon.getId());
                lmCouponsVo.setCouponType(lmCoupon.getCouponType());
                lmCouponsVo.setGoodsType(lmCoupon.getGoodsType());
                lmCouponsVo.setProductType(lmCoupon.getProductType());
                lmCouponsVo.setSellerId(lmCoupon.getSellerId());
                lmCouponsVo.setCouponName(lmCoupon.getCouponName());
                lmCouponsVo.setCouponValue(lmCoupon.getCouponValue());
                lmCouponsVo.setMinAmount(lmCoupon.getMinAmount());
                lmCouponsVo.setSendStartTime(lmCoupon.getSendStartTime());
                lmCouponsVo.setSendEndTime(lmCoupon.getSendEndTime());
                lmCouponsVo.setUseStartTime(lmCoupon.getUseStartTime());
                lmCouponsVo.setUseEndTime(lmCoupon.getUseEndTime());
                lmCouponsVo.setPersonLimitNum(lmCoupon.getPersonLimitNum());
                lmCouponsVo.setTotalLimitNum(lmCoupon.getTotalLimitNum());
                lmCouponsVo.setReceivedNum(lmCoupon.getReceivedNum());
                lmCouponsVo.setType(lmCoupon.getType());
                lmCouponsVo.setChannel(lmCoupon.getChannel());
                lmCouponsVo.setStatus(lmCoupon.getStatus());
                lmCouponsVo.setRemark(lmCoupon.getRemark());
                lmCouponsVo.setCreateTime(lmCoupon.getCreateTime());
                lmCouponsVo.setUpdateTime(lmCoupon.getUpdateTime());
                lmCouponsVo.setProductIds(lmCoupon.getProductIds());
                List<LmConpouMember> byMemberId = lmCouponMemberDao.findByMemberId(Integer.parseInt(memberId), lmCoupon.getId());
                if(byMemberId!=null&&byMemberId.size()>0){
                    if(byMemberId.size()>=lmCoupon.getPersonLimitNum()){
                        lmCouponsVo.setIsSlow(0);
                    }else {
                        for (LmConpouMember lmConpouMember : byMemberId) {
                            lmCouponsVo.setIsSlow(1);
                            if(lmConpouMember.getCanUse()==1){
                                lmCouponsVo.setIsSlow(0);
                                break;
                            }
                        }
                    }
                }else {
                    lmCouponsVo.setIsSlow(1);
                }
                lmCouponsVos.add(lmCouponsVo);
            }
        }
        return  lmCouponsVos;
    }
    @Override
    public List<LmCoupons> findAll() {
        return lmCouponsDao.selectAll();
    }

    @Override
    public void insertService(LmCoupons lmCoupons) {
        lmCouponsDao.insertSelective(lmCoupons);
    }

    @Override
    public void insertLogService(LmCouponLog lmCouponLog) {
        lmCouponLogDao.insert(lmCouponLog);
    }



    @Override
    public void updateService(LmCoupons lmCoupons) {
        lmCouponsDao.updateByPrimaryKeySelective(lmCoupons);
    }

    @Override
    public void deleteService(String id) {
        lmCouponsDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<Map<String, Object> > findByCondient(Map<String, Object> condient) {
        return lmCouponsDao.findByCondient(condient);
    }

    @Override
    public  List<Map<String, Object>> findMemberCouponByMId(Integer memberId, Integer merchId,Integer type) {
        List<Map<String, Object>> lmCouponsLists=new LinkedList<>();
        List<Map<String, Object>> lmCouponsList=lmCouponsDao.findMemberCouponByMId(memberId, merchId);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Map<String, Object> map:lmCouponsList){
            int canUse =(int)map.get("can_use");
            int couponType= (int)map.get("type");
            Date nowDate=new Date();
            nowDate.setTime(nowDate.getTime()+60*60l*1000);
            //0 可使用，
            if(0==type){
                if(1!=canUse){
                    map.clear();
                }else {
                    if (2 == couponType) {
                        Date useEndTime = (Date) map.get("useEndTime");
                        if (nowDate.getTime() > useEndTime.getTime()) {
                            map.clear();
                        } else {
                            Number sellerId = (Number) map.get("seller_id");
                            int intValue = sellerId.intValue();
                            LmMerchInfo lmMerchInfo = lmMerchInfoDao.selectByPrimaryKey(intValue);
                            map.put("storeName", lmMerchInfo.getStore_name());
                            map.put("useEndTime", dateFormat.format((Date) map.get("useEndTime")));
                            map.put("useStartTime", dateFormat.format((Date) map.get("useStartTime")));
                            map.put("receive_time", dateFormat.format((Date) map.get("receive_time")));
                            map.put("couponStatus", 0);
                            lmCouponsLists.add(map);
                        }
                    } else {
                        Calendar calendar = new GregorianCalendar();
                        Date receive_time = (Date) map.get("receive_time");
                        calendar.setTime(receive_time);
                        calendar.add(Calendar.SECOND, +14 * 24 * 3600);
                        receive_time = calendar.getTime();
                        if (nowDate.getTime() > receive_time.getTime()) {
                            map.clear();
                        } else {
                            map.put("storeName", null);
                            map.put("useEndTime", dateFormat.format(receive_time));
                            map.put("useStartTime", dateFormat.format((Date) map.get("receive_time")));
                            map.put("receive_time", dateFormat.format((Date) map.get("receive_time")));
                            map.put("couponStatus", 0);
                            lmCouponsLists.add(map);
                        }
                    }
                }
             //   1 已使用，
            }else if(1==type){
                if(0!=canUse){
                    map.clear();
                }else {
                    if(2==couponType){
                            Number  sellerId=(Number ) map.get("seller_id");
                            int intValue = sellerId.intValue();
                            LmMerchInfo lmMerchInfo = lmMerchInfoDao.selectByPrimaryKey(intValue);
                            map.put("storeName", lmMerchInfo.getStore_name());
                            map.put("useEndTime", dateFormat.format((Date)map.get("useEndTime")));
                            map.put("useStartTime", dateFormat.format((Date)map.get("useStartTime")));
                            map.put("receive_time", dateFormat.format((Date)map.get("receive_time")));
                            map.put("couponStatus", 1);
                            lmCouponsLists.add(map);
                    }else {
                        Calendar calendar = new GregorianCalendar();
                        Date receive_time=(Date)map.get("receive_time");
                        calendar.setTime(receive_time);
                        calendar.add(Calendar.SECOND,+14*24*3600);
                        receive_time=calendar.getTime();
                        map.put("storeName", null);
                        map.put("useEndTime", dateFormat.format(receive_time));
                        map.put("useStartTime",dateFormat.format((Date)map.get("receive_time")));
                        map.put("receive_time", dateFormat.format((Date)map.get("receive_time")));
                        map.put("couponStatus", 1);
                        lmCouponsLists.add(map);
                    }
                }
            //  2 已过期
            }else if(2==type){
                if(1!=canUse){
                    map.clear();
                }else {
                    if (2 == couponType) {
                        Date useEndTime = (Date) map.get("useEndTime");
                        if (nowDate.getTime() < useEndTime.getTime()) {
                            map.clear();
                        } else {
                            Number sellerId = (Number) map.get("seller_id");
                            int intValue = sellerId.intValue();
                            LmMerchInfo lmMerchInfo = lmMerchInfoDao.selectByPrimaryKey(intValue);
                            map.put("storeName", lmMerchInfo.getStore_name());
                            map.put("useEndTime", dateFormat.format((Date) map.get("useEndTime")));
                            map.put("useStartTime", dateFormat.format((Date) map.get("useStartTime")));
                            map.put("receive_time", dateFormat.format((Date) map.get("receive_time")));
                            map.put("couponStatus", 2);
                            lmCouponsLists.add(map);
                        }
                    } else {
                        Calendar calendar = new GregorianCalendar();
                        Date receive_time = (Date) map.get("receive_time");
                        calendar.setTime(receive_time);
                        calendar.add(Calendar.SECOND, +14 * 24 * 3600);
                        receive_time = calendar.getTime();
                        if (nowDate.getTime() < receive_time.getTime()) {
                            map.clear();
                        } else {
                            map.put("storeName", null);
                            map.put("useEndTime", dateFormat.format(receive_time));
                            map.put("useStartTime", dateFormat.format((Date) map.get("receive_time")));
                            map.put("receive_time", dateFormat.format((Date) map.get("receive_time")));
                            map.put("couponStatus", 2);
                            lmCouponsLists.add(map);
                        }
                    }
                }
            }
        }
        return lmCouponsLists;
    }

    @Override
    public HttpJsonResult insertNewCouponService(Integer memberId) {

        HttpJsonResult jsonResult=new HttpJsonResult<>();
        List<LmCoupons> newCouponByMId = lmCouponsDao.findNewCouponByMId(0);
        for(LmCoupons lmCoupons:newCouponByMId){
            List<LmConpouMember> lmConpouMembers = lmCouponMemberDao.findByMemberId(memberId,lmCoupons.getId());
            if(1 <= lmConpouMembers.size()){
                jsonResult.setCode(Errors.get_coupon_error.getCode());
                jsonResult.setMsg(Errors.get_coupon_error.getMsg());
                return jsonResult;
            }
            if(null!=lmConpouMembers&&lmConpouMembers.size()>0) {
                LmConpouMember isCanUse = lmConpouMembers.get(0);
                if (isCanUse.getCanUse() == 1) {
                    jsonResult.setCode(Errors.is_can_use.getCode());
                    jsonResult.setMsg(Errors.is_can_use.getMsg());
                    return jsonResult;
                }
            }
            Integer receivedNum = lmCoupons.getReceivedNum();
            if(receivedNum == null){
                receivedNum = 0;
            }
            if(lmCoupons.getTotalLimitNum() >= receivedNum){
                lmCoupons.setReceivedNum(++receivedNum);
                lmCouponsDao.updateByPrimaryKeySelective(lmCoupons);
            }else {
                jsonResult.setCode(Errors.get_coupon_error.getCode());
                jsonResult.setMsg(Errors.get_coupon_error.getMsg());
                return jsonResult;
            }
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            calendar.add(Calendar.SECOND, +14 * 24 * 3600);
            Date receive_time = calendar.getTime();
            if(lmCoupons.getStatus()==0){
            LmConpouMember lmConpouMember = new LmConpouMember();
            lmConpouMember.setMemberId(memberId);
            lmConpouMember.setSellerId(lmCoupons.getSellerId());
            lmConpouMember.setCouponId(lmCoupons.getId());
            lmConpouMember.setCanUse(1);
            lmConpouMember.setReceiveTime(new Date());
            lmConpouMember.setOrderId(0);
            lmConpouMember.setUseStartTime(new Date());
            lmConpouMember.setUseEndTime(receive_time);
            lmConpouMember.setCreateTime(new Date());
            lmConpouMember.setUpdateTime(new Date());
            lmConpouMember.setProductIds(null);
            lmCouponMemberDao.insert(lmConpouMember);
            }
            jsonResult.setCode(Errors.SUCCESS.getCode());
            jsonResult.setMsg(Errors.SUCCESS.getMsg());
        }
        return jsonResult;

    }

    public List<Map<String,Object>> orderCouponList(Integer memberId,String productPrice,String goodsType,String productType,String merId) {
        BigDecimal productPrices=new  BigDecimal(productPrice);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> orderCouponList = lmCouponsDao.orderCouponList(memberId);
        List<Map<String, Object>> orderCouponLists=new LinkedList<>();
        Date nowDate=new Date();
        nowDate.setTime(nowDate.getTime()+60*60l*1000);
        for(Map<String, Object> map:orderCouponList){
            int couponType=(int)map.get("type");
            int canUse=(int)map.get("can_use");
            if(1==canUse){
                if(2==couponType){
                    Number  nums=(Number ) map.get("goodsType");
                    int goodsTypes = nums.intValue();
                    if(Integer.parseInt(goodsType)==goodsTypes||2==goodsTypes){
                        Number  num=(Number ) map.get("productType");
                        int productTypes = num.intValue();
                            if(Integer.parseInt(productType)==productTypes||3==productTypes){
                            Date useEndTime=(Date)map.get("useEndTime");
                            Date useStartTime=(Date)map.get("useStartTime");
                                if(nowDate.getTime()>useEndTime.getTime()&&nowDate.getTime()<useStartTime.getTime()){
                                    map.clear();
                                }else {
                                    BigDecimal minAmount=(BigDecimal)map.get("minAmount");
                                    if(productPrices.compareTo(minAmount)>-1){
                                        Number  sellerId=(Number ) map.get("seller_id");
                                        int intValue = sellerId.intValue();
                                        if(intValue==Integer.parseInt(merId)){
                                        LmMerchInfo lmMerchInfo = lmMerchInfoDao.selectByPrimaryKey(intValue);
                                        map.put("storeName", lmMerchInfo.getStore_name());
                                        map.put("useEndTime", dateFormat.format((Date)map.get("useEndTime")));
                                        map.put("useStartTime", dateFormat.format((Date)map.get("useStartTime")));
                                        map.put("receive_time", dateFormat.format((Date)map.get("receive_time")));
                                        map.put("couponStatus", 0);
                                        orderCouponLists.add(map);
                                        }else {
                                            map.clear();
                                        }
                                    }else {
                                        map.clear();
                                    }
                                }
                            }else {
                                map.clear();
                            }
                    }else {
                        map.clear();
                    }
                }else {
                    Calendar calendar = new GregorianCalendar();
                    Date receive_time=(Date)map.get("receive_time");
                    calendar.setTime(receive_time);
                    calendar.add(Calendar.SECOND,+14*24*3600);
                    receive_time=calendar.getTime();
                    if(nowDate.getTime()>receive_time.getTime()){
                        map.clear();
                    }else {
                        BigDecimal minAmount=(BigDecimal)map.get("minAmount");
                        if(productPrices.compareTo(minAmount)>-1) {
                            map.put("storeName", null);
                            map.put("useEndTime", dateFormat.format(receive_time));
                            map.put("useStartTime", dateFormat.format((Date) map.get("receive_time")));
                            map.put("receive_time", dateFormat.format((Date) map.get("receive_time")));
                            map.put("couponStatus", 0);
                            orderCouponLists.add(map);
                        }
                    }
                }
            }else {
                map.clear();
            }
        }
        return orderCouponLists;
    }


    @Override
    public HttpJsonResult isEjectCoupon(Integer memberId) {
        HttpJsonResult jsonResult=new HttpJsonResult<>();
        jsonResult.setCode(Errors.SUCCESS.getCode());
        jsonResult.setMsg(Errors.SUCCESS.getMsg());
        List<LmCoupons> newCouponByMId = lmCouponsDao.findNewCouponByMId(0);
        Map<String , Object> map =new HashMap<>();
        BigDecimal total=new BigDecimal(0);
        if(newCouponByMId!=null&&newCouponByMId.size()>0){
           for (LmCoupons lmCoupons:newCouponByMId) {
               List<LmConpouMember> lmConpouMembers = lmCouponMemberDao.findByMemberId(memberId, lmCoupons.getId());
               if (1 <= lmConpouMembers.size()) {
                   map.put("status",0);
                   map.put("price",0);
                   jsonResult.setData(map);
                   return jsonResult;
               }
               total=total.add(lmCoupons.getCouponValue()).setScale(2,BigDecimal.ROUND_HALF_UP);
               map.put("status",1);
               map.put("price",total);
           }
        }
        jsonResult.setData(map);
        return jsonResult;
    }

    @Override
    public void addShareCoupon(String shareId) {
        List<LmCoupons> shareCoupon = lmCouponsDao.findNewCouponByMId(1);
        if(shareCoupon!=null&&shareCoupon.size()>0){
            for(LmCoupons lmCoupons:shareCoupon) {
                if(lmCoupons.getStatus()==0){
                    LmConpouMember lmConpouMember = new LmConpouMember();
                    lmConpouMember.setMemberId(Integer.parseInt(shareId));
                    lmConpouMember.setSellerId(lmCoupons.getSellerId());
                    lmConpouMember.setCouponId(lmCoupons.getId());
                    lmConpouMember.setCanUse(1);
                    lmConpouMember.setReceiveTime(new Date());
                    lmConpouMember.setOrderId(0);
                    lmConpouMember.setUseStartTime(null);
                    lmConpouMember.setUseEndTime(null);
                    lmConpouMember.setCreateTime(new Date());
                    lmConpouMember.setUpdateTime(new Date());
                    lmConpouMember.setProductIds(null);
                    lmCouponMemberDao.insert(lmConpouMember);
                }
            }
        }
    }

    @Override
    public List<LmCoupons> findNewCouponByMId(Integer type){
        return lmCouponsDao.findNewCouponByMId(type);
    }

    @Override
    public List<LmCoupons> findMerchCouponBys() {
        return lmCouponsDao.findMerchCouponBys();
    }


    @Override
    public List<LmCoupons> findNewCouponList() {
        return lmCouponsDao.findNewCouponList();
    }
}
