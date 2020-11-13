package com.wink.livemall.order.service.impl;

import com.wink.livemall.order.dao.LmPayLogDao;
import com.wink.livemall.order.dto.LmPayLog;
import com.wink.livemall.order.service.LmPayLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LmPayLogServiceImpl implements LmPayLogService {

    @Resource
    private LmPayLogDao lmPayLogDao;

    @Override
    public void insertService(LmPayLog lmPayLog) {
        lmPayLogDao.insert(lmPayLog);
    }

    @Override
    public LmPayLog findBymerOrderIdAndType(String merOrderId, String type) {
        return lmPayLogDao.findBymerOrderIdAndType(merOrderId,type);
    }
}
