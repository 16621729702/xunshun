package com.wink.livemall.sys.image.service.impl;

import com.wink.livemall.sys.image.dao.LmImageGroupDao;
import com.wink.livemall.sys.image.dto.LmImageGroup;
import com.wink.livemall.sys.image.service.LmImageGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class LmImageGroupServiceImpl implements LmImageGroupService {

    @Resource
    private LmImageGroupDao lmImageGroupDao;

    @Override
    public List<LmImageGroup> findAll() {
        return lmImageGroupDao.selectAll();
    }

    @Override
    public void insertService(LmImageGroup lmImageGroup) {
        lmImageGroupDao.insert(lmImageGroup);
    }

    @Override
    public void updateService(LmImageGroup lmImageGroup) {
        lmImageGroupDao.updateByPrimaryKey(lmImageGroup);
    }

    @Override
    public void deleteByPK(Integer id) {
        lmImageGroupDao.deleteByPrimaryKey(id);
    }

    @Override
    public LmImageGroup findByPk(String id) {
        return lmImageGroupDao.selectByPrimaryKey(Integer.parseInt(id));
    }
}
