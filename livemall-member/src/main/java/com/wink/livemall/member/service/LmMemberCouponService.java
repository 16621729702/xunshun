package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.LmMemberAddress;
import com.wink.livemall.member.dto.LmMemberCoupon;

import java.util.List;
import java.util.Map;

public interface LmMemberCouponService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    LmMemberCoupon findById(String id);

    /**
     * 查询所有
     * @return
     */
    List<LmMemberCoupon> findAll();

    /**
     * 添加操作
     * @param lmMemberCoupon
     */
    void insertService(LmMemberCoupon lmMemberCoupon);

    /**
     * 更新操作
     * @param lmMemberCoupon
     */
    void updateService(LmMemberCoupon lmMemberCoupon);

    /**
     * 根据主键删除
     * @param id
     */
    void deleteService(String id);

    List<LmMemberCoupon> findByMemberid(int userid);

    List<Map<String, String>> findByMemberidByApi(int userid);
}
