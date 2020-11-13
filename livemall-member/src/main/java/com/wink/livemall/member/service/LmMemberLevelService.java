package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberLevel;

import java.util.List;
import java.util.Map;

public interface LmMemberLevelService {
    /**
     * 根据主键查询
     * @param id
     * @return
     */
    LmMemberLevel findById(String id);

    /**
     * 查询所有
     * @return
     */
    List<LmMemberLevel> findAll();

    /**
     * 添加操作
     * @param lmMemberLevel
     */
    void insertService(LmMemberLevel lmMemberLevel);

    /**
     * 更新操作
     * @param lmMemberLevel
     */
    void updateService(LmMemberLevel lmMemberLevel);

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
    List<LmMemberLevel> findByCondient(Map<String, String> condient);

    List<Map<String, String>> findInfoByApi();
}
