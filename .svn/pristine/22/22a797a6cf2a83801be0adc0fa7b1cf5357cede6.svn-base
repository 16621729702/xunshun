package com.wink.livemall.merch.service;

import com.wink.livemall.merch.dto.LmMerchAdmin;
import com.wink.livemall.merch.dto.LmMerchApplyInfo;
import com.wink.livemall.merch.dto.LmMerchInfo;

import java.util.List;
import java.util.Map;

public interface LmMerchInfoService {

	/**
	 * 根据字段更新数据
	 * @param params
	 * @param id
	 */
	void updateByFields(List<Map<String, String>> params,String id);
	
	/**
	 * 检查商户是否存在是否可用
	 * @param id
	 * @return
	 */
	int checkMerchEnable(String id);
	
    /**
     * 根据主键查询
     * @param id
     * @return
     */
    LmMerchInfo findById(String id);

    /**
     * 查询所有
     * @return
     */
    List<LmMerchInfo> findAll();

    /**
     * 添加操作
     * @param lmMerchInfo
     */
    void insertService(LmMerchInfo lmMerchInfo);

    /**
     * 更新操作
     * @param lmMerchInfo
     */
    int updateService(LmMerchInfo lmMerchInfo);

    /**
     * 根据主键删除
     * @param id
     */
    void deleteService(String id);

    /**
     * 删除商户和商户认证信息
     * @param id
     */
    void deleteMerch(String id);

    /**
     * 商户认证
     * @param id
     * @param state
     */
    void check(String id, String state);

    List<LmMerchInfo> findByCondient(Map<String, String> condient);

    List<Map<String,Object>> findByCondient2(Map<String, String> condient);

    /**
     * 根据分类id查询商户信息
     * @param categoryid
     * @return
     */
    List<Map<String, Object>> findMerchInfoByCategoryByApi(int categoryid);

    Map<String, String> findMerchinfoByMerchidByApi(int merchid);

    Map<String, Object> findInfoByIdByApd(int id);

    List<LmMerchInfo>  findByMemberid(int id);

    LmMerchInfo findDirectMerch();

    List<Map<String, Object>> findMerchInfoByNameByApi(String name);

    /**
     * 查询所有正常的商户
     * @return
     */
    List<LmMerchInfo> findActiveMerch();

    int findmaxno();

    void addMerchAdmin(LmMerchAdmin lmMerchAdmin);

    boolean deleteMerchAdmin(String merchid, String memberid);

    List<Map<String, Object>> findAdminInfo(String merchid);

    List<LmMerchAdmin> findAdminByMember(int id);
}
