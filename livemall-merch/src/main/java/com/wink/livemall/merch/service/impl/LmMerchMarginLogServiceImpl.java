package com.wink.livemall.merch.service.impl;

import com.wink.livemall.merch.dao.LmMerchMarginLogDao;
import com.wink.livemall.merch.dto.LmMerchMarginLog;
import com.wink.livemall.merch.service.LmMerchMarginLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


@Service
public class LmMerchMarginLogServiceImpl implements LmMerchMarginLogService {
    @Resource
    private LmMerchMarginLogDao lmMerchMarginLogDao;


    @Override
    public List<LmMerchMarginLog> findByMerId(int merId, int type) {
        return lmMerchMarginLogDao.findByMerId(merId,type);
    }

    @Override
    public List<LmMerchMarginLog> findByMerIdAll(int merId) {
        return lmMerchMarginLogDao.findByMerIdAll(merId);
    }

    @Override
    public LmMerchMarginLog findByMarginSn(String  marginSn) {
        return lmMerchMarginLogDao.findByMarginSn(marginSn);
    }

    @Override
    public LmMerchMarginLog findByID(int id) {
        return lmMerchMarginLogDao.findByID(id);
    }

    @Override
    public  List<LmMerchMarginLog> findViolateAll() {
        return lmMerchMarginLogDao.findViolateAll();
    }

    @Override
    public BigDecimal merMarginSum(int merId) {
        List<LmMerchMarginLog> marginPayment = lmMerchMarginLogDao.findByMerId(merId, 0);
        BigDecimal payment=new BigDecimal(0);
        if(marginPayment.size()>0){
            for(LmMerchMarginLog lmMerchMarginLog:marginPayment){
                payment = payment.add(lmMerchMarginLog.getPrice());
            }
        }
        List<LmMerchMarginLog> marginDeduction = lmMerchMarginLogDao.findByMerId(merId, 1);
        BigDecimal deduction=new BigDecimal(0);
        if(marginDeduction.size()>0){
            for(LmMerchMarginLog lmMerchMarginLog:marginDeduction){
                deduction = deduction.add(lmMerchMarginLog.getPrice());
            }
        }
        BigDecimal merMarginSum = payment.subtract(deduction).setScale(2,BigDecimal.ROUND_HALF_UP);
        return merMarginSum;
    }


    @Override
    public void insert(LmMerchMarginLog lmMerchMarginLog) {
        lmMerchMarginLogDao.insertSelective(lmMerchMarginLog);
    }

    @Override
    public void update(LmMerchMarginLog lmMerchMarginLog) {
        lmMerchMarginLogDao.updateByPrimaryKeySelective(lmMerchMarginLog);
    }


}
