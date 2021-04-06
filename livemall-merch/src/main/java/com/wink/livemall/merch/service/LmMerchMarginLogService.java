package com.wink.livemall.merch.service;

import com.wink.livemall.merch.dto.LmMerchMarginLog;

import java.math.BigDecimal;
import java.util.List;

public interface LmMerchMarginLogService {


    List<LmMerchMarginLog> findByMerId(int merId, int type);

    List<LmMerchMarginLog> findByMerIdAll(int merId);

    LmMerchMarginLog findByMarginSn(String marginSn);

    LmMerchMarginLog findByID(int id);

    BigDecimal merMarginSum(int merId);

    void  insert(LmMerchMarginLog lmMerchMarginLog);

    void  update(LmMerchMarginLog lmMerchMarginLog);

    List<LmMerchMarginLog> findViolateAll();

}
