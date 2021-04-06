package com.wink.livemall.member.service.impl;

import com.wink.livemall.coupon.dao.LmCouponMemberDao;
import com.wink.livemall.coupon.dao.LmCouponsDao;
import com.wink.livemall.coupon.dto.LmConpouMember;
import com.wink.livemall.coupon.dto.LmCoupons;
import com.wink.livemall.member.dao.LmMemberInviteDao;
import com.wink.livemall.member.dto.LmMemberInvite;
import com.wink.livemall.member.service.LmMemberInviteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class LmMemberInviteServiceImpl implements LmMemberInviteService {

    @Resource
    private LmMemberInviteDao lmMemberInviteDao;
    @Resource
    private LmCouponMemberDao lmCouponMemberDao;
    @Resource
    private LmCouponsDao lmCouponsDao;

    @Override
    public LmMemberInvite getInviteCode(String memberId) {
        LmMemberInvite lmMemberInvite = lmMemberInviteDao.findByInviteCode(Integer.parseInt(memberId));
        if(lmMemberInvite==null){
            LmMemberInvite lmMemberInvites=new LmMemberInvite();
            lmMemberInvites.setMember_id(Integer.parseInt(memberId));
            lmMemberInvites.setInvite_num(0);
            String inviteCode= (memberId+(int)(Math.random() * (99999 - 10000) + 10000)).substring(0, 6);
            lmMemberInvites.setInvite_code(inviteCode);
            lmMemberInvites.setCreate_time(new Date());
            lmMemberInviteDao.insertSelective(lmMemberInvites);
            lmMemberInvite=lmMemberInvites;
        }
        return lmMemberInvite;
    }

    @Override
    public LmMemberInvite findByInviteCode(String memberId) {
        return lmMemberInviteDao.findByInviteCode(Integer.parseInt(memberId));
    }


    @Override
    public void addShareCoupon(String inviteCode, LmMemberInvite lmMemberInvite) {
        List<LmCoupons> shareCoupon = lmCouponsDao.findNewCouponByMId(1);
        LmMemberInvite forwardInvite = lmMemberInviteDao.findByMemberId(inviteCode);
        if(forwardInvite!=null){
            if(shareCoupon!=null&&shareCoupon.size()>0) {
                for (LmCoupons lmCoupons : shareCoupon) {
                    if(lmCoupons.getStatus()==0) {
                        LmConpouMember lmConpouMember = new LmConpouMember();
                        lmConpouMember.setMemberId(forwardInvite.getMember_id());
                        lmConpouMember.setSellerId(lmCoupons.getSellerId());
                        lmConpouMember.setCouponId(lmCoupons.getId());
                        lmConpouMember.setCanUse(1);
                        lmConpouMember.setReceiveTime(new Date());
                        lmConpouMember.setOrderId(0);
                        lmConpouMember.setCreateTime(new Date());
                        lmConpouMember.setUpdateTime(new Date());
                        lmCouponMemberDao.insertSelective(lmConpouMember);
                    }
                }
            }
            Integer invite_num = forwardInvite.getInvite_num();
            forwardInvite.setInvite_num(++invite_num);
            lmMemberInviteDao.updateByPrimaryKeySelective(forwardInvite);
            lmMemberInvite.setForward_id(forwardInvite.getMember_id());
            lmMemberInviteDao.updateByPrimaryKeySelective(lmMemberInvite);
        }
    }


}
