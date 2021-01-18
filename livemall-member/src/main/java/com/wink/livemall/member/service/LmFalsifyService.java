package com.wink.livemall.member.service;


import com.wink.livemall.member.dto.LmFalsify;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LmFalsifyService {

    LmFalsify findById(String id);


    /**
     * 添加操作
     * @param lmFalsify
     */
    void insertService(LmFalsify lmFalsify);


    /**
     * 更新操作
     * @param lmFalsify
     */
    void updateService(LmFalsify lmFalsify);


    /**
     * 根据主键删除
     * @param id
     */
    void deleteService(int id);

    /**
     * @param falsifyId
     * @param status
     * 列表
     * @return
     */
    List<Map<String, Object>> findFalsify(String falsifyId,String status);


    /**
     * @param falsifyId
     * @param goodid
     * @param goodstype
     * 寻找到违约金记录
     * @return
     */
    LmFalsify isFalsify(String falsifyId,String goodid,String goodstype);

    /**
     *
     * @param falsifyId
     * @return
     */
    LmFalsify findFalsifyId(String falsifyId);

    /**
     *
     * @param falsifyId
     * @return
     */
    Map<String,String>  isRefundFalsify(String falsifyId);

}
