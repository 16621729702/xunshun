package com.wink.livemall.coupon.service.impl;

import com.wink.livemall.coupon.dao.LmCouponLogDao;
import com.wink.livemall.coupon.dao.LmCouponsDao;
import com.wink.livemall.coupon.dto.LmCouponLog;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.coupon.service.LmCouponsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmCouponsServiceImpl implements LmCouponsService {

    @Resource
    private LmCouponsDao lmCouponsDao;
    @Resource
    private LmCouponLogDao lmCouponLogDao;

    @Override
    public LmCoupons findById(String id) {
        return lmCouponsDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmCoupons> findAll() {
        return lmCouponsDao.selectAll();
    }

    @Override
    public void insertService(LmCoupons lmCoupons) {
        lmCouponsDao.insert(lmCoupons);
    }

    @Override
    public void insertLogService(LmCouponLog lmCouponLog) {
        lmCouponLogDao.insert(lmCouponLog);
    }



    @Override
    public void updateService(LmCoupons lmCoupons) {
        lmCouponsDao.updateByPrimaryKey(lmCoupons);
    }

    @Override
    public void deleteService(String id) {
        lmCouponsDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<Map<String, Object> > findByCondient(Map<String, Object> condient) {
        return lmCouponsDao.findByCondient(condient);
    }
}
