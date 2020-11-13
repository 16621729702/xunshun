package com.wink.livemall.goods.service.impl;


import com.wink.livemall.goods.dao.LotsInfoDao;
import com.wink.livemall.goods.dao.LotsLogDao;
import com.wink.livemall.goods.dto.LmLotsInfo;
import com.wink.livemall.goods.dto.LmLotsLog;
import com.wink.livemall.goods.service.LotsService;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;


@Service
public class LotsServiceImp implements LotsService {

	@Resource
	private LotsInfoDao lotsInfoDao;
	
	@Resource
	private LotsLogDao lotsLogDao;

	@Override
	public void addLog(LmLotsLog record) {
		// TODO Auto-generated method stub
		lotsLogDao.insertSelective(record);
	}

	@Override
	public void addInfo(LmLotsInfo info) {
		// TODO Auto-generated method stub
		lotsInfoDao.insertSelective(info);
	}

	@Override
	public List<Map<String, Object>> findLotsInfo(int goodsid) {
		// TODO Auto-generated method stub
		return lotsInfoDao.findLotsInfo(goodsid);
	}

	@Override
	public void updateInfo(LmLotsInfo oldinfo) {
		lotsInfoDao.updateByPrimaryKey(oldinfo);
	}

	@Override
	public LmLotsInfo findLotsInfo2(int goodsid, int merchid, int type) {
		return lotsInfoDao.findLotsInfo2(goodsid,merchid,type);
	}


}
