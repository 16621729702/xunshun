package com.wink.livemall.sys.basic.service;

import com.wink.livemall.sys.basic.dto.LmBasicConfig;

import java.util.List;

public interface LmBasicConfigService {
    List<LmBasicConfig> findAll();

    LmBasicConfig findById(String id);

    void addService(LmBasicConfig lmBasicConfig);

    void updateService(LmBasicConfig lmBasicConfig);

    LmBasicConfig findByType(String yhxy);
}
