package com.wink.livemall.sys.basic.service.impl;

import com.wink.livemall.sys.basic.dao.LmBasicConfigDao;
import com.wink.livemall.sys.basic.dto.LmBasicConfig;
import com.wink.livemall.sys.basic.service.LmBasicConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LmBasicConfigServiceImpl implements LmBasicConfigService {

    @Resource
    private LmBasicConfigDao lmBasicConfigDao;

    /**
     * 查询所有协议
     * @return
     */
    @Override
    public List<LmBasicConfig> findAll() {
        return lmBasicConfigDao.selectAll();
    }

    @Override
    public LmBasicConfig findById(String id) {
        return lmBasicConfigDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void addService(LmBasicConfig lmBasicConfig) {
        lmBasicConfigDao.insert(lmBasicConfig);
    }

    @Override
    public void updateService(LmBasicConfig lmBasicConfig) {
        lmBasicConfigDao.updateByPrimaryKey(lmBasicConfig);
    }

    @Override
    public LmBasicConfig findByType(String type) {
        return lmBasicConfigDao.findByType(type);
    }
}
