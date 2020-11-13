package com.wink.livemall.merch.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import com.wink.livemall.merch.dao.LmMerchBlackDao;

import com.wink.livemall.merch.dto.LmMerchBlack;

import com.wink.livemall.merch.service.LmMerchBlackService;


@Service
public class LmMerchBlackServiceImpl implements LmMerchBlackService{
	@Resource
	private LmMerchBlackDao lmMerchBlackDao;
	


	@Override
	public List<Map<String, Object>> findlist(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return lmMerchBlackDao.getpage(params);
	}



	@Override
	public void delBlack(int id) {
		// TODO Auto-generated method stub
		lmMerchBlackDao.deleteByPrimaryKey(id);
	}



	@Override
	public void addBlack(LmMerchBlack entity) {
		// TODO Auto-generated method stub
		lmMerchBlackDao.insert(entity);
	}


	@Override
	public int findExistMerch(Map<String, Object> params) {
		
		return lmMerchBlackDao.countByCondition(params);
	}
	
	

}
