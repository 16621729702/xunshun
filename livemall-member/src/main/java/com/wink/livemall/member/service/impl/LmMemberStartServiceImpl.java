package com.wink.livemall.member.service.impl;

import com.wink.livemall.member.dao.LmMemberStartDao;
import com.wink.livemall.member.dto.LmMemberStart;
import com.wink.livemall.member.service.LmMemberStartService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LmMemberStartServiceImpl implements LmMemberStartService {


    @Resource
    private LmMemberStartDao lmMemberStartDao;


    @Override
    public List<LmMemberStart> findByMobile(String mobile, int type) {
        return lmMemberStartDao.findByMobile(mobile,type);
    }

    @Override
    public void delete(int id) {
        lmMemberStartDao.deleteByPrimaryKey(id);
    }

    @Override
    public void updateMobile(LmMemberStart lmMemberStart) {
        lmMemberStartDao.updateByPrimaryKeySelective(lmMemberStart);
    }

    @Override
    public void insert(LmMemberStart lmMemberStart) {
        lmMemberStartDao.insertSelective(lmMemberStart);
    }


}
