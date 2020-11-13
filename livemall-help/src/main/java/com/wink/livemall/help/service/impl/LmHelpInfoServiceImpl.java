package com.wink.livemall.help.service.impl;

import com.wink.livemall.help.dao.LmHelpInfoDao;
import com.wink.livemall.help.dto.LmHelpInfo;
import com.wink.livemall.help.service.LmHelpInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmHelpInfoServiceImpl implements LmHelpInfoService {


    @Resource
    private LmHelpInfoDao lmHelpInfoDao;

    @Override
    public LmHelpInfo findById(String id) {
        return lmHelpInfoDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmHelpInfo> findAll() {
        return lmHelpInfoDao.selectAll();
    }

    @Override
    public void insertService(LmHelpInfo lmHelpInfo) {
        lmHelpInfoDao.insert(lmHelpInfo);
    }

    @Override
    public void updateService(LmHelpInfo lmHelpInfo) {
        lmHelpInfoDao.updateByPrimaryKey(lmHelpInfo);
    }

    @Override
    public void deleteService(String id) {
        lmHelpInfoDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<Map<String, String>> findBycategoryid(String categoryid) {
        return lmHelpInfoDao.findBycategoryid(categoryid);
    }
}
