package com.wink.livemall.merch.service;

import com.wink.livemall.merch.dto.LmMerchConfigs;

public interface LmMerchConfigsService {

    LmMerchConfigs findByType(String type);

    void insertService(LmMerchConfigs lmMerchConfigs);

    LmMerchConfigs findById(String id);

    void updateService(LmMerchConfigs lmMerchConfigs);
}
