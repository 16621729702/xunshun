package com.wink.livemall.live.service;

import com.wink.livemall.live.dto.LmLive;

import java.util.List;
import java.util.Map;

public interface LmMerchLiveService {

    LmLive findLive(int id);
    
    LmLive findLiveByMerchid(int id);
    
    void addLiveApply(LmLive live);
	
    void updLive(LmLive live);
    
  
}
