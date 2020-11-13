package com.wink.livemall.merch.service;

import com.wink.livemall.merch.dto.LmMerchApplyInfo;

import java.util.List;
import java.util.Map;

public interface LmMerchApplyInfoService {


    /**
     * 根据主键查询
     * @param id
     * @return
     */
    LmMerchApplyInfo findById(String id);

    /**
     * 查询所有
     * @return
     */
    List<LmMerchApplyInfo> findAll();

    /**
     * 添加操作
     * @param lmMerchApplyInfo
     */
    void insertService(LmMerchApplyInfo lmMerchApplyInfo);

    /**
     * 更新操作
     * @param lmMerchApplyInfo
     */
    void updateService(LmMerchApplyInfo lmMerchApplyInfo);

    /**
     * 根据主键删除
     * @param id
     */
    void deleteService(String id);

    List<LmMerchApplyInfo> findByCondient(Map<String, String> condient);
}
