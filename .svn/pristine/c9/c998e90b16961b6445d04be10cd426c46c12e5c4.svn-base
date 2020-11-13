package com.wink.livemall.coupon.service.impl;

import com.wink.livemall.coupon.dao.LmCouponsDao;
import com.wink.livemall.coupon.dao.LmMerchCouponsDao;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponsService;
import com.wink.livemall.coupon.service.LmMerchCouponsService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmMerchCouponsServiceImpl implements LmMerchCouponsService {

    @Resource
    private LmMerchCouponsDao lmMerchCouponsDao;

    @Override
    public LmCoupons findById(String id) {
        return lmMerchCouponsDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmCoupons> findAll() {
        return lmMerchCouponsDao.selectAll();
    }

    @Override
    public void insertService(LmCoupons lmCoupons) {
    	lmMerchCouponsDao.insert(lmCoupons);
    }

    @Override
    public void updateService(LmCoupons lmCoupons) {
    	lmMerchCouponsDao.updateByPrimaryKey(lmCoupons);
    }

    @Override
    public void deleteService(String id) {
    	lmMerchCouponsDao.deleteByPrimaryKey(Integer.parseInt(id));
    }



	@Override
	public int countEnabled(Map<String, String> params) {
		// TODO Auto-generated method stub
		return lmMerchCouponsDao.countEnabled(params);
	}


	@Override
	public List<Map<String, Object>> findEnabled(Map<String, String> params) {
		// TODO Auto-generated method stub
		return lmMerchCouponsDao.findEnabled(params);
	}

  
}
