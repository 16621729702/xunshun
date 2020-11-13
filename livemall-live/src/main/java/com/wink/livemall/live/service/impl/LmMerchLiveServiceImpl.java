package com.wink.livemall.live.service.impl;

import com.wink.livemall.live.dao.LmLiveDao;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.service.LmLiveService;
import com.wink.livemall.live.service.LmMerchLiveService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmMerchLiveServiceImpl implements LmMerchLiveService {

    @Resource
    private LmLiveDao lmLiveDao;

	@Override
	public LmLive findLive(int id) {
		// TODO Auto-generated method stub
		return lmLiveDao.selectByPrimaryKey(id);
	}

	@Override
	public void addLiveApply(LmLive live) {
		// TODO Auto-generated method stub
		lmLiveDao.insertSelective(live);
	}

	@Override
	public LmLive findLiveByMerchid(int id) {
		// TODO Auto-generated method stub
	
		return lmLiveDao.findLiveByMerchid(id);
	}

	@Override
	public void updLive(LmLive live) {
		// TODO Auto-generated method stub
		lmLiveDao.updateByPrimaryKeySelective(live);
	}


}
