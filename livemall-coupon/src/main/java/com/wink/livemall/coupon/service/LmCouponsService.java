package com.wink.livemall.coupon.service;


import com.wink.livemall.coupon.dto.LmCouponLog;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.dto.vo.LmCouponsVo;
import com.wink.livemall.utils.HttpJsonResult;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface LmCouponsService {
    /**
     * 根据主键查询
     * @param id
     * @return
     */
    LmCoupons findById(String id);

    List<LmCoupons> findMerchCoupon(Integer merchId,Integer status);


    List<LmCouponsVo> findMerchCouponUsable(Integer merchId, Integer status, String memberId);

    /**
     * 查询所有
     * @return
     */
    List<LmCoupons> findAll();

    /**
     * 添加操作
     * @param lmCoupons
     */
    void insertService(LmCoupons lmCoupons);

    /**
     * 添加日志操作
     * @param lmCouponLog
     */
    void insertLogService(LmCouponLog lmCouponLog);



    /**
     * 更新操作
     * @param lmCoupons
     */
    void updateService(LmCoupons lmCoupons);

    /**
     * 根据主键删除
     * @param id
     */
    void deleteService(String id);

    /**
     * 根据条件查询
     * @param condient
     * @return
     */
    List<Map<String, Object> > findByCondient(Map<String, Object> condient);


    List<Map<String, Object>> findMemberCouponByMId(Integer memberId,Integer merchId,Integer type);


    /**
     * 添加新人优惠卷操作
     * @param memberId
     */
    HttpJsonResult insertNewCouponService(Integer memberId);


    /**
     *
     * @param memberId
     * @param productPrice
     * @param goodsTypes
     * @param productType
     * @param merId
     * @return
     */
    List<Map<String,Object>> orderCouponList(Integer memberId,String productPrice,String goodsTypes,String productType,String merId);


    /**
     * 是否弹出新人优惠券
     * @param memberId
     * @return
     */
    HttpJsonResult  isEjectCoupon(Integer memberId);


    /**
     * 查询所有
     * @return
     */
    void addShareCoupon(String shareId);

    List<LmCoupons> findNewCouponByMId(Integer type);

    List<LmCoupons> findMerchCouponBys();

    List<LmCoupons> findNewCouponList();

}
