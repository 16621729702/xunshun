package com.wink.livemall.coupon.service;

import com.wink.livemall.coupon.dto.LmCoupons;

import java.util.List;
import java.util.Map;

public interface LmMerchCouponsService {
	/**
	 * 根据主键查询
	 * 
	 * @param id
	 * @return
	 */
	LmCoupons findById(String id);

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	List<LmCoupons> findAll();

	
	/**
	 * 添加操作
	 * 
	 * @param lmCoupons
	 */
	void insertService(LmCoupons lmCoupons);

	/**
	 * 更新操作
	 * 
	 * @param lmCoupons
	 */
	void updateService(LmCoupons lmCoupons);

	/**
	 * 根据主键删除
	 * 
	 * @param id
	 */
	void deleteService(String id);

	/**
	 * 查询可用批次数量
	 * @return
	 */
	int countEnabled(Map<String, String> params);
	
	
	/**
	 * 查询可用优惠券批次
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> findEnabled(Map<String, String> params);

}
