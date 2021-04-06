package com.wink.livemall.live.service;


import com.wink.livemall.live.dto.LmLiveRenew;
import org.apache.ibatis.annotations.Param;

public interface LmLiveRenewService {

    void addLivePay(LmLiveRenew lmLiveRenew);

    LmLiveRenew findLmLiveRenew(String livePaySn);

    void updLivePay(LmLiveRenew lmLiveRenew);

}
