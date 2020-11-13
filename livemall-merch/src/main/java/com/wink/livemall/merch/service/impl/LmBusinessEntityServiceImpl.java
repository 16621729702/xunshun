package com.wink.livemall.merch.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wink.livemall.merch.dao.LmBusinessEntityDao;
import com.wink.livemall.merch.dto.LmBusinessEntity;
import com.wink.livemall.merch.service.LmBusinessEntityService;

import tk.mybatis.mapper.entity.Example;

@Service
public class LmBusinessEntityServiceImpl implements LmBusinessEntityService{
	@Resource
	private LmBusinessEntityDao lmBusinessEntityDao;
	

	@Override
	public List<LmBusinessEntity> findByCondient(Example example) {
		// TODO Auto-generated method stub
		return lmBusinessEntityDao.selectByExample(example);
	}

}
