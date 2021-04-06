package com.wink.livemall.member.service.impl;

import com.wink.livemall.member.dao.ForwardUserDao;
import com.wink.livemall.member.dto.ForwardUser;
import com.wink.livemall.member.service.ForwardUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ForwardUserServiceImpl implements ForwardUserService {

    @Resource
    private ForwardUserDao forwardUserDao;


    @Override
    public void insertForwardUser(ForwardUser forwardUser) {
        forwardUser.setCreate_time(new Date());
        forwardUser.setUpdate_time(new Date());
        forwardUserDao.insertSelective(forwardUser);
    }

    @Override
    public void updateForwardUser(ForwardUser forwardUser) {
        forwardUser.setUpdate_time(new Date());
        forwardUserDao.updateByPrimaryKeySelective(forwardUser);
    }

    @Override
    public ForwardUser findListByUserId(int userId) {
        return forwardUserDao.findListByUserId(userId);
    }

    @Override
    public List<ForwardUser> findListByForwardId(int forwardId) {
        return forwardUserDao.findListByForwardId(forwardId);
    }
}
