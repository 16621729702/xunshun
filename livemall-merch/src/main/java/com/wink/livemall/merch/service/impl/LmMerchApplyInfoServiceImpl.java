package com.wink.livemall.merch.service.impl;

import com.wink.livemall.merch.dao.LmMerchApplyInfoDao;
import com.wink.livemall.merch.dto.LmMerchApplyInfo;
import com.wink.livemall.merch.service.LmMerchApplyInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmMerchApplyInfoServiceImpl implements LmMerchApplyInfoService {

    @Resource
    private LmMerchApplyInfoDao lmMerchApplyInfoDao;

    @Override
    public LmMerchApplyInfo findById(String id) {
        return lmMerchApplyInfoDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMerchApplyInfo> findAll() {
        return lmMerchApplyInfoDao.selectAll();
    }

    @Override
    public void insertService(LmMerchApplyInfo lmMerchApplyInfo) {
        lmMerchApplyInfoDao.insert(lmMerchApplyInfo);
    }

    @Override
    public void updateService(LmMerchApplyInfo lmMerchApplyInfo) {
        lmMerchApplyInfoDao.updateByPrimaryKey(lmMerchApplyInfo);
    }

    @Override
    public void deleteService(String id) {
        lmMerchApplyInfoDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMerchApplyInfo> findByCondient(Map<String, String> condient) {
        return lmMerchApplyInfoDao.findByCondient(condient);
    }
}
