package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.LmMemberCoupon;
import com.wink.livemall.member.dto.LmMemberFav;

import java.util.List;
import java.util.Map;

public interface LmMemberFavService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    LmMemberFav findById(String id);

    /**
     * 查询所有
     * @return
     */
    List<LmMemberFav> findAll();

    /**
     * 添加操作
     * @param lmMemberFav
     */
    void insertService(LmMemberFav lmMemberFav);

    /**
     * 更新操作
     * @param lmMemberFav
     */
    void updateService(LmMemberFav lmMemberFav);

    /**
     * 根据主键删除
     * @param id
     */
    void deleteService(int id);

    List<LmMemberFav> findByMemberid(int parseInt);

    List<Map<String, String>> findInfoByMemberid(int userid );

    LmMemberFav findByMemberidAndGoodid(String userid, String goodid);

    LmMemberFav findByMemberidAndVideoidByApi(int id, int id1);

    void addService(LmMemberFav lmMemberFav);

    List<Map<String, String>> findInfoByMemberidByApi(int userid, int type);

    LmMemberFav findByMemberidAndTypeAndId(int userid, int type, int id);
}
