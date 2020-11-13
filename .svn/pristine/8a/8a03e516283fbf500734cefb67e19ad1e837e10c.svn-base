package com.wink.livemall.merch.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wink.livemall.merch.dao.LmAucitonBidDao;
import com.wink.livemall.merch.dao.LmAucitonDepositDao;
import com.wink.livemall.merch.dao.LmBusinessEntityDao;
import com.wink.livemall.merch.dao.LmMerchFollowDao;
import com.wink.livemall.merch.dao.LmSellCateDao;
import com.wink.livemall.merch.dto.LmAuctionBid;
import com.wink.livemall.merch.dto.LmAuctionDeposit;
import com.wink.livemall.merch.dto.LmBusinessEntity;
import com.wink.livemall.merch.dto.LmMerchFollow;
import com.wink.livemall.merch.dto.LmSellCate;
import com.wink.livemall.merch.service.LmAuctionBidService;
import com.wink.livemall.merch.service.LmAuctionDepositService;
import com.wink.livemall.merch.service.LmBusinessEntityService;
import com.wink.livemall.merch.service.LmMerchFollowService;
import com.wink.livemall.merch.service.LmSellCateService;

import tk.mybatis.mapper.entity.Example;

@Service
public class LmMerchFollowServiceImpl implements LmMerchFollowService{
	@Resource
	private LmMerchFollowDao lmMerchFollowDao;
	
	

	@Override
	public List<Map<String, Object>> findlist(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return lmMerchFollowDao.getpage(params);
	}
	
	

}
