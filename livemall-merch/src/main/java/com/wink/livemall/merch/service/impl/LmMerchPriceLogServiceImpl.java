package com.wink.livemall.merch.service.impl;

import com.wink.livemall.merch.dao.LmMerchPriceLogDao;
import com.wink.livemall.merch.dto.LmMerchPriceLog;
import com.wink.livemall.merch.service.LmMerchPriceLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class LmMerchPriceLogServiceImpl implements LmMerchPriceLogService {

    @Resource
    private LmMerchPriceLogDao lmMerchPriceLogDao;

    @Override
    public void insertService(LmMerchPriceLog lmMerchPriceLog) {
        lmMerchPriceLogDao.insertSelective(lmMerchPriceLog);
    }
}
