package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.LmMemberLink;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LmMemberLinkService {

    void insert(LmMemberLink lmMemberLink);


    List<LmMemberLink> findMemberIdOrLinkId(int memberId);

    void delMemberLink(int id);

    List<LmMemberLink> findLink(int memberId,int linkId);

}
