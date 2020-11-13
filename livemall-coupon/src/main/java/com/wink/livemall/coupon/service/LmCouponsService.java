package com.wink.livemall.coupon.service;

import com.wink.livemall.coupon.dto.LmCouponLog;
import com.wink.livemall.coupon.dto.LmCoupons;

import java.util.List;
import java.util.Map;

public interface LmCouponsService {
    /**
     * 根据主键查询
     * @param id
     * @return
     */
    LmCoupons findById(String id);

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
}
