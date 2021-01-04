package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.LmMemberFav;
import com.wink.livemall.member.dto.LmMemberFollow;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LmMemberFollowService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    LmMemberFollow findById(String id);

    /**
     * 查询所有
     * @return
     */
    List<LmMemberFollow> findAll();

    /**
     * 添加操作
     * @param lmMemberFollow
     */
    void insertService(LmMemberFollow lmMemberFollow);

    /**
     * 更新操作
     * @param lmMemberFollow
     */
    void updateService(LmMemberFollow lmMemberFollow);

    /**
     * 根据主键删除
     * @param id
     */
    void deleteService(int id);

    List<LmMemberFollow> findByMemberid(int memberid);

    List<Map<String, Object>> findByMemberidAndType(int userid, String type);


    /**
	 * 获取粉丝列表
	 * @return
	 */
	List<Map<String, Object>> findlist(Map<String, Object> params);


    void addService(LmMemberFollow lmMemberFollow);

    LmMemberFollow findByMemberidAndMerchId(String userid, int id);

    LmMemberFollow findByMemberidAndTypeAndId(int userid, int type, int id);

    int countNumYed(String merchid);

    List<LmMemberFollow> findByMerchidAndType(int i, int id);

    List<LmMemberFollow> findByMerchidCount(int i, int id,int userid);
}
