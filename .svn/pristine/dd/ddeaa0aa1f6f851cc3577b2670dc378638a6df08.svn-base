package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.LmMemberFollow;
import com.wink.livemall.member.dto.LmMemberTrace;

import java.util.List;
import java.util.Map;

public interface LmMemberTraceService {
    /**
     * 根据主键查询
     * @param id
     * @return
     */
    LmMemberTrace findById(String id);

    /**
     * 查询所有
     * @return
     */
    List<LmMemberTrace> findAll();

    /**
     * 添加操作
     * @param lmMemberTrace
     */
    void insertService(LmMemberTrace lmMemberTrace);

    /**
     * 更新操作
     * @param lmMemberTrace
     */
    void updateService(LmMemberTrace lmMemberTrace);

    /**
     * 根据主键删除
     * @param id
     */
    void deleteService(String id);

    /**
     * 根据会员id查询想关信息
     * @param memberid
     * @return
     */
    List<LmMemberTrace> findByMemberid(int memberid);

    /**
     * 获取足迹
     * @param userid
     * @param type
     * @return
     */
    List<Map<String,Object>>  findByMemberidAndType(int userid, int type);

    LmMemberTrace findByMemberidAndGoodid(int userid, int id);

    LmMemberTrace findByMemberidAndLiveid(int userid, int liveid);
}
