package com.wink.livemall.goods.service;

import java.util.List;
import java.util.Map;

import com.wink.livemall.goods.dto.LmLotsInfo;
import com.wink.livemall.goods.dto.LmLotsLog;

public interface LotsService {
	
	/**
	 * 添加抽签记录
	 */
	void addLog(LmLotsLog record);
	
	/**
	 *添加开料记录
	 */
	void addInfo(LmLotsInfo info);
	
	List<Map<String, Object>> findLotsInfo(int goodsid);

    void updateInfo(LmLotsInfo oldinfo);

	LmLotsInfo findLotsInfo2(int goodsid, int merchid, int type);
}