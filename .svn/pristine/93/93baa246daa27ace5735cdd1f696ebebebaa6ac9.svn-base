package com.wink.livemall.merch.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wink.livemall.merch.dao.LmBusinessEntityDao;
import com.wink.livemall.merch.dao.LmSellCateDao;
import com.wink.livemall.merch.dto.LmBusinessEntity;
import com.wink.livemall.merch.dto.LmSellCate;
import com.wink.livemall.merch.service.LmBusinessEntityService;
import com.wink.livemall.merch.service.LmSellCateService;

import tk.mybatis.mapper.entity.Example;

@Service
public class LmSellCateServiceImpl implements LmSellCateService{
	@Resource
	private LmSellCateDao lmSellCateDao;
	


	@Override
	public List<LmSellCate> findlist() {
		// TODO Auto-generated method stub
		return lmSellCateDao.findlist();
	}

	@Override
	public LmSellCate findById(int id) {
		return lmSellCateDao.selectByPrimaryKey(id);
	}

	@Override
	public void insertServuce(LmSellCate sellCate) {
		lmSellCateDao.insert(sellCate);
	}

	@Override
	public void deletethis(int id) {
		lmSellCateDao.deleteByPrimaryKey(id);
	}

	@Override
	public void updateService(LmSellCate sellCate) {
		lmSellCateDao.updateByPrimaryKey(sellCate);
	}

}
