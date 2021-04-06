package com.wink.livemall.sys.withdraw.service;

import com.wink.livemall.sys.withdraw.dto.AllBankInfo;

import java.util.List;

public interface AllBankInfoService {

    void insertAllBankInfo(AllBankInfo allBankInfo);

    void updateAllBankInfo(AllBankInfo allBankInfo);

    AllBankInfo findListByCode(int code);

    List<AllBankInfo> findAll();

}
