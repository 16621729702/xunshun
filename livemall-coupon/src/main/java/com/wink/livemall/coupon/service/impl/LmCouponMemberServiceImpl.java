package com.wink.livemall.coupon.service.impl;

import com.wink.livemall.coupon.dao.LmCouponMemberDao;
import com.wink.livemall.coupon.dto.LmConpouMember;
import com.wink.livemall.coupon.service.LmCouponMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LmCouponMemberServiceImpl implements LmCouponMemberService {
    @Resource
    private LmCouponMemberDao lmCouponMemberDao;


    @Override
    public LmConpouMember findById(Integer id) {
        return lmCouponMemberDao.selectByPrimaryKey(id);
    }

    @Override
    public int insert(LmConpouMember lmConpouMember) {
        return lmCouponMemberDao.insert(lmConpouMember);
    }

}
