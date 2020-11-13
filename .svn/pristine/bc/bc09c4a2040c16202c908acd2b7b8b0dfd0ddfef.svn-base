package com.wink.livemall.sys.setting.service.impl;

import com.wink.livemall.sys.setting.dao.ConfigsDao;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ConfigsServiceImpl implements ConfigsService {

    @Resource
    private ConfigsDao configsDao;
    //根据类型查询
    @Override
    public Configs findByTypeId(int type_basic) {
        return configsDao.findByTypeId(type_basic);
    }

    /**
     * 添加
     * @param configs
     */
    @Override
    public void insertService(Configs configs) {
        configsDao.insert(configs);
    }

    @Override
    public void updateService(Configs configs) {
        configsDao.updateByPrimaryKey(configs);
    }

    @Override
    public Configs findById(int id) {
        return configsDao.selectByPrimaryKey(id);
    }

}
