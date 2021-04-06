package com.wink.livemall.live.service.impl;


import com.wink.livemall.live.dao.LmLiveRenewDao;
import com.wink.livemall.live.dto.LmLiveRenew;
import com.wink.livemall.live.service.LmLiveRenewService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class LmLiveRenewServiceImpl implements LmLiveRenewService {

    @Resource
    private LmLiveRenewDao lmLiveRenewDao;

    @Override
    public void addLivePay(LmLiveRenew lmLiveRenew) {
        lmLiveRenew.setState(0);
        lmLiveRenew.setType(0);
        lmLiveRenew.setCreate_time(new Date());
        lmLiveRenewDao.insertSelective(lmLiveRenew);
    }

    @Override
    public LmLiveRenew findLmLiveRenew(String livePaySn) {
        return lmLiveRenewDao.findLmLiveRenew(livePaySn);
    }

    @Override
    public void updLivePay(LmLiveRenew lmLiveRenew) {
        lmLiveRenewDao.updateByPrimaryKeySelective(lmLiveRenew);
    }
}
