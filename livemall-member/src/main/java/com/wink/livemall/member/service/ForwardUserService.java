package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.ForwardUser;

import java.util.List;

public interface ForwardUserService {

    void insertForwardUser(ForwardUser forwardUser);

    void updateForwardUser(ForwardUser forwardUser);

    /**
     * 查询
     * @param userId
     * @return
     */
    ForwardUser findListByUserId(int userId);

    /**
     * 查询
     * @param forwardId
     * @return
     */
    List<ForwardUser> findListByForwardId(int forwardId);
}
