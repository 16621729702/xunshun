package com.wink.livemall.sys.image.service.impl;

import com.wink.livemall.sys.image.dao.LmImageCoreDao;
import com.wink.livemall.sys.image.dto.LmImageCore;
import com.wink.livemall.sys.image.service.LmImageCoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmImageCoreServiceImpl implements LmImageCoreService {

    @Resource
    private LmImageCoreDao lmImageCoreDao;

    @Override
    public List<LmImageCore> findAll() {
        return lmImageCoreDao.selectAll();
    }

    @Override
    public void deletebyPK(Integer id) {
        lmImageCoreDao.deleteByPrimaryKey(id);
    }

    @Override
    public void insertService(LmImageCore lmImageCore) {
        lmImageCoreDao.insert(lmImageCore);
    }

    @Override
    public List<LmImageCore> findByCondient(Map<String, Object> condient) {
        return lmImageCoreDao.findByCondient(condient);
    }
}
