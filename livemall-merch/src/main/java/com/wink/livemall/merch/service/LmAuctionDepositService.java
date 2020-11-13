package com.wink.livemall.merch.service;


import com.wink.livemall.merch.dto.LmAuctionDeposit;

import java.util.List;

public interface LmAuctionDepositService {
	
	
	/**
	 * 获取主营类目
	 * @return
	 */
	List<LmAuctionDeposit> findlist();
    

}
