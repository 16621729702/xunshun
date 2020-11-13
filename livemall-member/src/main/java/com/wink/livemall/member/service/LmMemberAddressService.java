package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberAddress;

import java.util.List;
import java.util.Map;

public interface LmMemberAddressService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    LmMemberAddress findById(String id);

    /**
     * 查询所有
     * @return
     */
    List<LmMemberAddress> findAll();

    /**
     * 添加操作
     * @param lmMemberAddress
     */
    void insertService(LmMemberAddress lmMemberAddress);

    /**
     * 更新操作
     * @param lmMemberAddress
     */
    void updateService(LmMemberAddress lmMemberAddress);

    /**
     * 根据主键删除
     * @param id
     */
    void deleteService(int id);

    List<LmMemberAddress> findByMemberid(int parseInt);

    List<Map<String,String>> findByMemberidByapi(int parseInt);
}
