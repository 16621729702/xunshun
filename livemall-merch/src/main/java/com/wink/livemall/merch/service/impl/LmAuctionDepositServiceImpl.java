package com.wink.livemall.merch.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wink.livemall.merch.dao.LmAucitonDepositDao;
import com.wink.livemall.merch.dao.LmBusinessEntityDao;
import com.wink.livemall.merch.dao.LmSellCateDao;
import com.wink.livemall.merch.dto.LmAuctionDeposit;
import com.wink.livemall.merch.dto.LmBusinessEntity;
import com.wink.livemall.merch.dto.LmSellCate;
import com.wink.livemall.merch.service.LmAuctionDepositService;
import com.wink.livemall.merch.service.LmBusinessEntityService;
import com.wink.livemall.merch.service.LmSellCateService;

import tk.mybatis.mapper.entity.Example;

@Service
public class LmAuctionDepositServiceImpl implements LmAuctionDepositService{
	@Resource
	private LmAucitonDepositDao lmAucitonDepositDao;
	


	@Override
	public List<LmAuctionDeposit> findlist() {
		// TODO Auto-generated method stub
		return lmAucitonDepositDao.findlist();
	}
	
	

}
