package com.wink.livemall.live.service.impl;

import com.wink.livemall.live.dao.LmLiveGoodDao;
import com.wink.livemall.live.dto.LmLiveGood;
import com.wink.livemall.live.service.LmLiveGoodService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LmLiveGoodServiceImpl implements LmLiveGoodService {

	@Resource
	private LmLiveGoodDao lmLiveGoodDao;
	
	@Override
	public void addGood(LmLiveGood good) {
		// TODO Auto-generated method stub
		lmLiveGoodDao.insert(good);
	}

	@Override
	public int checkGood(Map<String, String> params) {
		// TODO Auto-generated method stub
		Example example=new Example(LmLiveGood.class);
		Criteria criteria=example.createCriteria();
		criteria.andCondition("good_id="+params.get("good_id"));
		criteria.andCondition("liveid="+params.get("liveid"));
		return lmLiveGoodDao.selectCountByExample(example);
	}

	@Override
	public void delGood(int id) {
		// TODO Auto-generated method stub
		lmLiveGoodDao.deleteByPrimaryKey(id);
	}

	@Override
	public List<Map<String, Object>> findPage(Map<String, String> params) {
		// TODO Auto-generated method stub
		return lmLiveGoodDao.getpage(params);
	}

	@Override
	public List<Map<String, Object>> findByLiveIdByApi(int liveid) {
		return lmLiveGoodDao.findshareByLiveIdByApi(liveid);
	}

	@Override
	public List<Map<String, Object>> findByLiveIdByApi(String liveid, String type) {
		return lmLiveGoodDao.findByLiveIdByApi(Integer.parseInt(liveid),Integer.parseInt(type));
	}

	@Override
	public List<Map<String, Object>> findAllInfo() {
		return lmLiveGoodDao.findAllInfo();
	}

	@Override
	public List<Map<String, Object>> findInfoByLiveidAndName(Map<String, String> condient) {
		return lmLiveGoodDao.findInfoByLiveidAndName(condient);
	}

	@Override
	public List<LmLiveGood> findByLiveid(String liveid) {
		return lmLiveGoodDao.findByLiveid(Integer.parseInt(liveid));
	}

	@Override
	public LmLiveGood checkGood(String good_id, String liveid) {
		return lmLiveGoodDao.checkGood(Integer.parseInt(good_id),Integer.parseInt(liveid));
	}

	@Override
	public List<Map<String, Object>> findLiveGoodByLiveid(int liveid) {
		return lmLiveGoodDao.findLiveGoodByLiveid(liveid);
	}

	@Override
	public List<Map<String, Object>> findLivegoodinfoById(String liveid, String type) {
		return lmLiveGoodDao.findLivegoodinfoById(Integer.parseInt(liveid),Integer.parseInt(type));
	}

	@Override
	public List<Map<String, Object>> findLivegoodtomemberid(String liveid, String type,String userid) {
		return lmLiveGoodDao.findLivegoodtomemberid(Integer.parseInt(liveid),Integer.parseInt(type),Integer.parseInt(userid));
	}

	@Override
	public List<LmLiveGood> findlivegoodByGoodid(int id) {
		return lmLiveGoodDao.findlivegoodByGoodid(id);
	}


}
