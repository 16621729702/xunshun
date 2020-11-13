package com.wink.livemall.merch.service;

import com.wink.livemall.merch.dto.LmSellCate;
import com.wink.livemall.merch.dto.LmWithdrawal;

import java.util.List;
import java.util.Map;

public interface LmWithdrawalService {
    List<Map<String,Object>> findList();


    Map<String, Object> findinfoById(int id);

    LmWithdrawal findById(int id);

    void updateService(LmWithdrawal lmWithdrawal);

    void insertservice(LmWithdrawal lmWithdrawal);

}
