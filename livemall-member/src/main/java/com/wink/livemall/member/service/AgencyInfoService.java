package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.AgencyInfo;

public interface AgencyInfoService {

    void insertAgencyInfo(AgencyInfo agencyInfo);

    void updateAgencyInfo(AgencyInfo agencyInfo);

    AgencyInfo findListByUserId(int UserId);
}
