package com.wink.livemall.merch.service.impl;

import com.wink.livemall.merch.dao.LmWithdrawalDao;
import com.wink.livemall.merch.dto.LmSellCate;
import com.wink.livemall.merch.dto.LmWithdrawal;
import com.wink.livemall.merch.service.LmWithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmWithdrawalServiceImpl implements LmWithdrawalService {

    @Resource
    private LmWithdrawalDao lmWithdrawalDao;

    @Override
    public List<Map<String,Object>> findList() {
        return lmWithdrawalDao.findInfoList();
    }

    @Override
    public Map<String, Object> findinfoById(int id) {
        return lmWithdrawalDao.findinfoById(id);
    }

    @Override
    public LmWithdrawal findById(int id) {
        return lmWithdrawalDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateService(LmWithdrawal lmWithdrawal) {
        lmWithdrawalDao.updateByPrimaryKey(lmWithdrawal);
    }

    @Override
    public void insertservice(LmWithdrawal lmWithdrawal) {
        lmWithdrawalDao.insert(lmWithdrawal);
    }
}
