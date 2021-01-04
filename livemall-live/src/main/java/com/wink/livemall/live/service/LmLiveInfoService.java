package com.wink.livemall.live.service;

import com.wink.livemall.live.dto.LmLiveInfo;

public interface LmLiveInfoService {

    LmLiveInfo findLiveInfo(int liveid);

    void insertService(LmLiveInfo lmLiveInfo);

    int updateService(LmLiveInfo lmLiveInfo);
}
