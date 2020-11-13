package com.wink.livemall.merch.service;


import com.wink.livemall.merch.dto.LmAuctionBid;
import com.wink.livemall.merch.dto.LmAuctionDeposit;

import java.util.List;

public interface LmAuctionBidService {
	
	
	/**
	 * 获取主营类目
	 * @return
	 */
	List<LmAuctionBid> findlist();
    

}
