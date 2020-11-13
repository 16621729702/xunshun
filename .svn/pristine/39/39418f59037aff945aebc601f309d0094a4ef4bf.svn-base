package com.wink.livemall.goods.service;

import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.dto.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MerchGoodService {
	Map<String, Object> findGoodByid(String id);
	
	List<Map<String, Object>> findWarehouses(String ids);
	
	int countByStatus(Map<String, String> params);
	
	/**
	 * 添加商品
	 * 
	 * @param good
	 */
	void addGoods(Good good);

	/**
	 * 修改
	 * 
	 * @param params
	 * @param id
	 */
	void updateByFields(List<Map<String, String>> params, String id);

	/**
	 * 商品列表分页
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getPage(Map<String, String> params);

	/**
	 * 删除商品
	 * 
	 * @param id
	 */
	void delGoods(int id);

	/**
	 * 统计商品数量
	 * 
	 * @param merchid
	 * @return
	 */
	int countGoodsNum(int merchid);

	/**
	 * 统计订单数量
	 * 
	 * @param merchid
	 * @return
	 */
	int countOrderNum(int merchid);

	/**
	 * 统计订单金额
	 * 
	 * @param merchid
	 * @return
	 */
	Integer countOrderPrice(int merchid);

	/**
	 * 根据对象更新
	 * 
	 * @param good
	 */
	void updateEntity(Good good);

	/**
	 * 获取出价最新记录
	 * 
	 * @return
	 */
	LmGoodAuction findLastAuction(int goodid);

	/**
	 * 竞拍中商品
	 * @param goodid
	 * @return
	 */
	int countAuctionGoods(int merchid);
	
	/**
	 * @param params
	 * @return
	 * 条件查询
	 */
	List<Map<String, Object>> findListByCondition(Map<String, String> params);
	
	

}