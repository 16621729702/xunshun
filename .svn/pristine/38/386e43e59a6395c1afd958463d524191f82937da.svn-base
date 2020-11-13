package com.wink.livemall.order.service.impl;

import com.wink.livemall.order.dao.LmShopOrderLogDao;
import com.wink.livemall.order.dto.LmOrderLog;
import com.wink.livemall.order.service.LmOrderLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LmShopOrderLogServiceImpl implements LmOrderLogService {
    @Resource
    private LmShopOrderLogDao lmShopOrderLogDao;

    @Override
    public List<LmOrderLog> findByOrderid(String orderid) {
        return lmShopOrderLogDao.findByOrderid(orderid);
    }

    @Override
    public void insert(LmOrderLog lmOrderLog) {
        lmShopOrderLogDao.insert(lmOrderLog);
    }
}
