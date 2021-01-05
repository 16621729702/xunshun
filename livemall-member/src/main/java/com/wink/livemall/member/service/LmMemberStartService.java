package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.LmMemberStart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LmMemberStartService {


    List<LmMemberStart> findByMobile(String mobile, int type);

    void delete(int id);

    void updateMobile(LmMemberStart lmMemberStart);

    void insert(LmMemberStart lmMemberStart);
}
