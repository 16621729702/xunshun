package com.wink.livemall.member.service.impl;

import com.wink.livemall.member.dao.LmMemberLevelDao;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberLevel;
import com.wink.livemall.member.service.LmMemberLevelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmMemberLevelServiceImpl implements LmMemberLevelService {

    @Resource
    private LmMemberLevelDao lmMemberLevelDao;

    @Override
    public LmMemberLevel findById(String id) {
        return lmMemberLevelDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMemberLevel> findAll() {
        return lmMemberLevelDao.selectAll();
    }

    @Override
    public void insertService(LmMemberLevel lmMemberLevel) {
        lmMemberLevelDao.insert(lmMemberLevel);
    }

    @Override
    public void updateService(LmMemberLevel lmMemberLevel) {
        lmMemberLevelDao.updateByPrimaryKeySelective(lmMemberLevel);
    }

    @Override
    public void deleteService(String id) {
        lmMemberLevelDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMemberLevel> findByCondient(Map<String, String> condient) {
        return lmMemberLevelDao.findByCondient(condient);
    }

    @Override
    public List<Map<String, String>> findInfoByApi() {
        return lmMemberLevelDao.findInfoByApi();
    }
}
