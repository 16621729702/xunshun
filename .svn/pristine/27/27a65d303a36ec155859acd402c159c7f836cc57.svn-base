package com.wink.livemall.merch.service;

import java.util.List;
import java.util.Map;

import com.wink.livemall.merch.dto.LmMerchBlack;

import tk.mybatis.mapper.entity.Example;

public interface LmMerchBlackService {
	
	
	/**
	 * 获取分数列表
	 * @return
	 */
	List<Map<String, Object>> findlist(Map<String, Object> params);
	
	/**
	 * 删除黑名单
	 */
	void delBlack(int id);
    
	/**
	 * 加入黑名单
	 */
	void addBlack(LmMerchBlack entity);
	
	/**
	 * 查询商户添加的客户黑名单是否存在
	 * @param params
	 * @return
	 */
	int findExistMerch(Map<String, Object> params);
	

}
