package com.wink.livemall.merch.service.impl;

import com.wink.livemall.merch.dao.LmMerchConfigsDao;
import com.wink.livemall.merch.dto.LmMerchConfigs;
import com.wink.livemall.merch.service.LmMerchConfigsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LmMerchConfigsServiceImpl implements LmMerchConfigsService {

    @Resource
    private LmMerchConfigsDao lmMerchConfigsDao;

    @Override
    public LmMerchConfigs findByType(String type) {
        return lmMerchConfigsDao.findByType(type);
    }

    @Override
    public void insertService(LmMerchConfigs lmMerchConfigs) {
        lmMerchConfigsDao.insert(lmMerchConfigs);
    }

    @Override
    public LmMerchConfigs findById(String id) {
        return lmMerchConfigsDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void updateService(LmMerchConfigs lmMerchConfigs) {
        lmMerchConfigsDao.updateByPrimaryKey(lmMerchConfigs);
    }
}
