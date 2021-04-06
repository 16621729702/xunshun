package com.wink.livemall.coupon.service.impl;

import com.wink.livemall.coupon.dao.LmCouponMemberDao;
import com.wink.livemall.coupon.dto.LmConpouMember;
import com.wink.livemall.coupon.service.LmCouponMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LmCouponMemberServiceImpl implements LmCouponMemberService {
    @Resource
    private LmCouponMemberDao lmCouponMemberDao;


    @Override
    public LmConpouMember findById(Integer id) {
        return lmCouponMemberDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateService(LmConpouMember lmConpouMember) {
        return lmCouponMemberDao.updateByPrimaryKeySelective(lmConpouMember);
    }

    @Override
    public List<LmConpouMember> findByMemberId(Integer memberId,Integer couponId) {
        return lmCouponMemberDao.findByMemberId(memberId,couponId);
    }

    @Override
    public int insert(LmConpouMember lmConpouMember) {
        return lmCouponMemberDao.insert(lmConpouMember);
    }

    @Override
    public LmConpouMember findByOrderId(Integer orderId) {
        return lmCouponMemberDao.findByOrderId(orderId) ;
    }

}
