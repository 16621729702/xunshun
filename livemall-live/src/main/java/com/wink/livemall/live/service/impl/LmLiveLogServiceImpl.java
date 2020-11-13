package com.wink.livemall.live.service.impl;

import com.wink.livemall.live.dao.LmLiveGoodDao;
import com.wink.livemall.live.dao.LmLiveLogDao;
import com.wink.livemall.live.dto.LmLiveGood;
import com.wink.livemall.live.dto.LmLiveLog;
import com.wink.livemall.live.service.LmLiveGoodService;
import com.wink.livemall.live.service.LmLiveLogService;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LmLiveLogServiceImpl implements LmLiveLogService {

	@Autowired
	private LmLiveLogDao lmLiveLogDao;

	@Override
	public List<LmLiveLog> findPage(Map<String, String> params) {
		// TODO Auto-generated method stub
		return lmLiveLogDao.findPage(params);
	}

	@Override
	public void addLog(LmLiveLog liveLog) {
		// TODO Auto-generated method stub
		lmLiveLogDao.insertSelective(liveLog);
	}

	@Override
	public LmLiveLog findLastLog(String merchid) {
		// TODO Auto-generated method stub
		return lmLiveLogDao.findLastLog(merchid);
	}

	@Override
	public void upd(LmLiveLog liveLog) {
		// TODO Auto-generated method stub
		lmLiveLogDao.updateByPrimaryKeySelective(liveLog);
	}

	@Override
	public Long countTime(String merchid) {
		// TODO Auto-generated method stub
		return lmLiveLogDao.countTime(merchid);
	}
	
	
}
