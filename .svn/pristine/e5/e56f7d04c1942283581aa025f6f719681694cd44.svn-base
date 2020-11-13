package com.wink.livemall.live.service.impl;

import com.wink.livemall.live.dao.LmLiveCategoryDao;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.dto.LmLiveCategory;
import com.wink.livemall.live.service.LmLiveCategoryService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmLiveCategoryServiceImpl implements LmLiveCategoryService {

    @Resource
    private LmLiveCategoryDao lmLiveCategoryDao;

    @Override
    public List<LmLiveCategory> findTopListByApi() {
        return lmLiveCategoryDao.findTopListByApi();
    }

	@Override
	public List<LmLiveCategory> findActiveList() {
		// TODO Auto-generated method stub
		Example example=new Example(LmLive.class);
		Criteria criteria=example.createCriteria();
		criteria.andCondition("status=0");
		return lmLiveCategoryDao.selectByExample(example);
	}

	@Override
	public List<LmLiveCategory> findByCondient(Map<String, String> condient) {
		return lmLiveCategoryDao.findByCondient(condient);
	}

	@Override
	public LmLiveCategory findbyId(String id) {
		return lmLiveCategoryDao.selectByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public void insertService(LmLiveCategory lmLiveCategory) {
		lmLiveCategoryDao.insert(lmLiveCategory);
	}

	@Override
	public void deleteService(int id) {
		lmLiveCategoryDao.deleteByPrimaryKey(id);
	}

	@Override
	public void updateService(LmLiveCategory lmLiveCategory) {
		lmLiveCategoryDao.updateByPrimaryKeySelective(lmLiveCategory);
	}
}
