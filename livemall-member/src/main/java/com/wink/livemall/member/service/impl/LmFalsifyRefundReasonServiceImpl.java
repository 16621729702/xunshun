package com.wink.livemall.member.service.impl;

import com.wink.livemall.member.dao.LmFalsifyRefundReasonDao;
import com.wink.livemall.member.dto.LmFalsifyRefundReason;
import com.wink.livemall.member.service.LmFalsifyRefundReasonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LmFalsifyRefundReasonServiceImpl implements LmFalsifyRefundReasonService {

    @Resource
    private LmFalsifyRefundReasonDao lmFalsifyRefundReasonDao;


    @Override
    public LmFalsifyRefundReason findRefundReason(int memberId, int goodId, int type) {
        return lmFalsifyRefundReasonDao.findRefundReason(memberId,goodId,type);
    }

    @Override
    public void insert(LmFalsifyRefundReason lmFalsifyRefundReason) {
        lmFalsifyRefundReasonDao.insertSelective(lmFalsifyRefundReason);
    }


    @Override
    public void update(LmFalsifyRefundReason lmFalsifyRefundReason) {
        lmFalsifyRefundReasonDao.updateByPrimaryKeySelective(lmFalsifyRefundReason);
    }

}
