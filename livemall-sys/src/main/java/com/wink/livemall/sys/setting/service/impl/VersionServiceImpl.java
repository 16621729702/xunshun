package com.wink.livemall.sys.setting.service.impl;

import com.wink.livemall.sys.setting.dao.VersionDao;
import com.wink.livemall.sys.setting.dao.VersionIOSDao;
import com.wink.livemall.sys.setting.dto.Version;
import com.wink.livemall.sys.setting.dto.VersionIOS;
import com.wink.livemall.sys.setting.service.VersionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VersionServiceImpl implements VersionService {
    @Resource
    private VersionDao versionDao;
    @Resource
    private VersionIOSDao versionIOSDao;


    @Override
    public void insertService(Version version) {
        versionDao.insert(version);
    }

    @Override
    public void updateService(Version version) {
        versionDao.updateByPrimaryKey(version);
    }

    @Override
    public void deleteService(int id) {
        versionDao.deleteByPrimaryKey(id);
    }

    @Override
    public Version findById(int id) {
        return versionDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Version> findByList() {
        return versionDao.selectAll();
    }


   @Override
   public Version findActive() {
       return versionDao.findActive();
   }

     @Override
    public VersionIOS findActiveIOS() {
        return versionIOSDao.findActiveIOS();
    }
}
