package com.wink.livemall.goods.service.impl;

import com.wink.livemall.goods.dao.GoodDao;
import com.wink.livemall.goods.dao.GoodSpecDao;
import com.wink.livemall.goods.dao.GoodSpecItemDao;
import com.wink.livemall.goods.dao.LmGoodAuctionDao;
import com.wink.livemall.goods.dao.MerchGoodDao;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.service.MerchGoodService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import org.springframework.stereotype.Service;


import javax.annotation.Resource;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class MerchGoodServiceImpl implements MerchGoodService {

    @Resource
    private MerchGoodDao MerchGoodDao;
    
    @Resource
    private LmGoodAuctionDao lmGoodAuctionDao;


    
    
    
	@Override
	public void addGoods(Good good) {
		MerchGoodDao.insertSelective(good);
	}


	@Override
	public void updateByFields(List<Map<String, String>> params, String id) {
		// TODO Auto-generated method stub
		MerchGoodDao.updateByFields(params, id);
	}


	@Override
	public List<Map<String, Object>> getPage(Map<String, String> params) {
		// TODO Auto-generated method stub
		return MerchGoodDao.getpage(params);
	}


	@Override
	public void delGoods(int id) {
		// TODO Auto-generated method stub
		MerchGoodDao.deleteByPrimaryKey(id);
	}


	@Override
	public int countGoodsNum(int merchid) {
		// TODO Auto-generated method stub
		return MerchGoodDao.countGoodsNum(merchid);
	}


	@Override
	public int countOrderNum(int merchid) {
		// TODO Auto-generated method stub
		return MerchGoodDao.countOrderNum(merchid);
	}


	@Override
	public Integer countOrderPrice(int merchid) {
		// TODO Auto-generated method stub
		return MerchGoodDao.countOrderPrice(merchid);
	}


	@Override
	public void updateEntity(Good good) {
		// TODO Auto-generated method stub
		MerchGoodDao.updateByPrimaryKeySelective(good);
	}


	@Override
	public LmGoodAuction findLastAuction(int goodid) {
		// TODO Auto-generated method stub
		
		return MerchGoodDao.findLast(goodid);
	}


	@Override
	public int countAuctionGoods(int merchid) {
		// TODO Auto-generated method stub
		return MerchGoodDao.countAuctionGoods(merchid, new Date());
	}


	@Override
	public List<Map<String, Object>> findListByCondition(Map<String, String> params) {
		// TODO Auto-generated method stub
		return MerchGoodDao.findListByCondition(params);
	}


	@Override
	public int countByStatus(Map<String, String> params) {
		// TODO Auto-generated method stub
		return MerchGoodDao.countByStatus(params);
	}


	@Override
	public List<Map<String, Object>> findWarehouses(String ids) {
		// TODO Auto-generated method stub
		return MerchGoodDao.findWarehouses(ids);
	}


	@Override
	public Map<String, Object> findGoodByid(String id) {
		// TODO Auto-generated method stub
		return MerchGoodDao.findByid(id);
	}

  


}
