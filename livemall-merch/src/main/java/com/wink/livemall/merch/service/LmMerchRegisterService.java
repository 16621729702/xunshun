package com.wink.livemall.merch.service;

import com.wink.livemall.merch.dto.LmMerchApplyInfo;
import com.wink.livemall.merch.dto.LmMerchInfo;

import java.util.List;
import java.util.Map;

public interface LmMerchRegisterService {


    /**
     * 更新操作
     * @param lmMerchInfo
     */
    int updateService(LmMerchInfo lmMerchInfo);

    /**
     * 添加商户注册信息 返回id
     * @param info
     * @return
     */
    int addMerchInfoGetId(LmMerchInfo info);

    /**
     * 添加商户注册信息
     * @param info
     */
    int addMerchInfo(LmMerchInfo info);
   
  
   /**
    * 根据id获取商户
	 * @param id
	 * @return
	 */
	LmMerchInfo findMerchInfoByid(int id);


    /**
     * 根据id获取商户
     * @param store_name
     * @return
     */
    int isRepeat(String  store_name);

}
