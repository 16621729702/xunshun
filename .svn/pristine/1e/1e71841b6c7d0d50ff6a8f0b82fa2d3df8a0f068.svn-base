package com.wink.livemall.sys.setting.service.impl;

import com.wink.livemall.sys.setting.dao.LideshowDao;
import com.wink.livemall.sys.setting.dto.Lideshow;
import com.wink.livemall.sys.setting.service.ExpressService;
import com.wink.livemall.sys.setting.service.LideshowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LideshowServiceImpl implements LideshowService {

    @Resource
    private LideshowDao lideshowDao;

    @Override
    public Lideshow findById(String id) {
        return lideshowDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void insertService(Lideshow lideshow) {
        lideshowDao.insert(lideshow);
    }

    @Override
    public void updateService(Lideshow lideshow) {
        lideshowDao.updateByPrimaryKey(lideshow);
    }

    @Override
    public List<Lideshow> findByCondient(Map<String, String> condient) {
        return lideshowDao.findByCondient(condient);
    }

    @Override
    public List<Lideshow> findListBytype(int type) {
        return lideshowDao.findListBytype(type);
    }

    @Override
    public void deleteService(String id) {
        lideshowDao.deleteByPrimaryKey(Integer.parseInt(id));
    }


}
