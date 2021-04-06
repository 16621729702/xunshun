package com.wink.livemall.sys.withdraw.service.impl;

import com.wink.livemall.sys.withdraw.dao.AllBankInfoDao;
import com.wink.livemall.sys.withdraw.dto.AllBankInfo;
import com.wink.livemall.sys.withdraw.service.AllBankInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class AllBankInfoServiceImpl implements AllBankInfoService {

    @Resource
    private AllBankInfoDao allBankInfoDao;


    @Override
    public void insertAllBankInfo(AllBankInfo allBankInfo) {
        allBankInfoDao.insertSelective(allBankInfo);
    }

    @Override
    public void updateAllBankInfo(AllBankInfo allBankInfo) {
        allBankInfoDao.updateByPrimaryKeySelective(allBankInfo);
    }


    @Override
    public AllBankInfo findListByCode(int code) {
        return allBankInfoDao.findListByCode(code);
    }

    @Override
    public List<AllBankInfo> findAll() {
        return allBankInfoDao.findAll();
    }
}
