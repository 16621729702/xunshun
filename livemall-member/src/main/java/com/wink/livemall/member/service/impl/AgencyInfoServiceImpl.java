package com.wink.livemall.member.service.impl;

import com.wink.livemall.member.dao.AgencyInfoDao;
import com.wink.livemall.member.dto.AgencyInfo;
import com.wink.livemall.member.service.AgencyInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class AgencyInfoServiceImpl implements AgencyInfoService {

    @Resource
    private AgencyInfoDao agencyInfoDao;

    @Override
    public void insertAgencyInfo(AgencyInfo agencyInfo) {
        agencyInfo.setCreate_time(new Date());
        agencyInfo.setUpdate_time(new Date());
        agencyInfoDao.insertSelective(agencyInfo);
    }

    @Override
    public void updateAgencyInfo(AgencyInfo agencyInfo) {
        agencyInfo.setUpdate_time(new Date());
        agencyInfoDao.updateByPrimaryKeySelective(agencyInfo);
    }

    @Override
    public AgencyInfo findListByUserId(int UserId) {
        return agencyInfoDao.findListByUserId(UserId);
    }
}
