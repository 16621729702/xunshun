package com.wink.livemall.live.service.impl;

import com.wink.livemall.live.dao.LmLiveInfoDao;
import com.wink.livemall.live.dto.LmLiveInfo;
import com.wink.livemall.live.service.LmLiveInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LmLiveInfoServiceImpl implements LmLiveInfoService {


    @Resource
    private LmLiveInfoDao lmLiveInfoDao;


    @Override
    public LmLiveInfo findLiveInfo(int liveid) {
        return lmLiveInfoDao.findLiveInfo(liveid);
    }

    @Override
    public void insertService(LmLiveInfo lmLiveInfo) {
        lmLiveInfoDao.insertSelective(lmLiveInfo);
    }

    @Override
    public int updateService(LmLiveInfo lmLiveInfo) {
        return lmLiveInfoDao.updateByPrimaryKeySelective(lmLiveInfo);
    }
}
