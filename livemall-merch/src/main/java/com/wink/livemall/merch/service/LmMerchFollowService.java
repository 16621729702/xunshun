package com.wink.livemall.merch.service;


import com.wink.livemall.merch.dto.LmMerchFollow;

import java.util.List;
import java.util.Map;

public interface LmMerchFollowService {
	
	
	/**
	 * 获取分数列表
	 * @return
	 */
	List<Map<String, Object>> findlist(Map<String, Object> params);
    

}
