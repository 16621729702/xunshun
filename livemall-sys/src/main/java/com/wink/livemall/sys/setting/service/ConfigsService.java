package com.wink.livemall.sys.setting.service;

import com.wink.livemall.sys.setting.dto.Configs;

public interface ConfigsService {

    Configs findByTypeId(int type_basic);

    void insertService(Configs configs);

    void updateService(Configs configs);

    Configs findById(int parseInt);
}
