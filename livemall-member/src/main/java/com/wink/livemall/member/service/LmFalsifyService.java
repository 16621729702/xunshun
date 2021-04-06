package com.wink.livemall.member.service;


import com.wink.livemall.member.dto.LmFalsify;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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
     * @param memberId
     * @param status
     * 用户列表
     * @return
     */
    List<Map<String, Object>> findFalsify(String memberId,String status);

    /**
     * @param merchId
     * @param status
     * 商户列表
     * @return
     */
    List<Map<String, Object>> getMerchFalsifyList(String merchId,String status);

    /**
     * @param memberid
     * @param goodid
     * @param goodstype
     * 寻找到违约金记录
     * @return
     */
    LmFalsify isFalsify(String memberid,String goodid,String goodstype);

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


    List<Map<String,Object>>  autoRefundFalsify(int goodId,int goodsType);

    //退款中 商家未处理
    List<LmFalsify> findNoFalsify();

   //总
    BigDecimal  falsifySum(int merId);

    List<Map<String,Object>> merFalsifyList(Integer merId);
}
