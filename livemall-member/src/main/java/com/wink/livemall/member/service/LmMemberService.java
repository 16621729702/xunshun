package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberLog;

import java.util.List;
import java.util.Map;

public interface LmMemberService {


    /**
     * 根据主键查询
     * @param id
     * @return
     */
    LmMember findById(String id);

    /**
     * 查询所有
     * @return
     */
    List<LmMember> findAll();

    /**
     * 添加操作
     * @param lmMember
     */
    void insertService(LmMember lmMember);

    /**
     * 更新操作
     * @param lmMember
     */
    void updateService(LmMember lmMember);

    /**
     * 根据主键删除
     * @param id
     */
    void deleteService(String id);

    /**
     * 根据条件查询
     * @param condient
     * @return
     */
    List<LmMember> findByCondient(Map<String, String> condient);

    /**
     * 根据手机号码查询用户
     * @param mobile
     * @return
     */
    LmMember findByMobile(String mobile);
    
    
    void addMemberLog(LmMemberLog entity);
}
