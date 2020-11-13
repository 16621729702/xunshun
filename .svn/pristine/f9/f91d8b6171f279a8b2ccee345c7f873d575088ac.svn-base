package com.wink.livemall.merch.dao;


import com.wink.livemall.merch.dto.LmAuctionDeposit;
import com.wink.livemall.merch.dto.LmSellCate;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface LmAucitonDepositDao extends tk.mybatis.mapper.common.Mapper<LmAuctionDeposit>{

	@Select("SELECT * FROM lm_auction_deposit")
	List<LmAuctionDeposit> findlist();
   
}
