package com.wink.livemall.member.service.impl;

import com.wink.livemall.member.dao.LmMemberCouponDao;
import com.wink.livemall.member.dto.LmMemberCoupon;
import com.wink.livemall.member.service.LmMemberCouponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmMemberCouponServiceImpl implements LmMemberCouponService {

    @Resource
    private LmMemberCouponDao lmMemberCouponDao;

    @Override
    public LmMemberCoupon findById(String id) {
        return lmMemberCouponDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMemberCoupon> findAll() {
        return lmMemberCouponDao.selectAll();
    }

    @Override
    public void insertService(LmMemberCoupon lmMemberCoupon) {
        lmMemberCouponDao.insert(lmMemberCoupon);
    }

    @Override
    public void updateService(LmMemberCoupon lmMemberCoupon) {
        lmMemberCouponDao.updateByPrimaryKeySelective(lmMemberCoupon);
    }

    @Override
    public void deleteService(String id) {
        lmMemberCouponDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMemberCoupon> findByMemberid(int parseInt) {
        return lmMemberCouponDao.findByMemberid(parseInt);
    }

}
