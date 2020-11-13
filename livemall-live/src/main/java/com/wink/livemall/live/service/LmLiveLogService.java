package com.wink.livemall.live.service;

import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.dto.LmLiveLog;

import java.util.List;
import java.util.Map;

public interface LmLiveLogService {

    List<LmLiveLog> findPage(Map<String, String> params);
    
    void addLog(LmLiveLog liveLog);
    
    LmLiveLog findLastLog(String merchid);
    
    void upd(LmLiveLog liveLog);
    
    Long countTime(String merchid);
}
