package com.wink.livemall.video.service.impl;

import com.wink.livemall.video.dao.LmVideoCategoaryDao;
import com.wink.livemall.video.dto.LmVideoCategoary;
import com.wink.livemall.video.service.LmVideoCategoaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmVideoCategoaryServiceImpl implements LmVideoCategoaryService {

    @Resource
    private LmVideoCategoaryDao lmVideoCategoaryDao;

    @Override
    public LmVideoCategoary findById(String id) {
        return lmVideoCategoaryDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void addService(LmVideoCategoary lmVideoCategoary) {
        lmVideoCategoaryDao.insert(lmVideoCategoary);
    }

    @Override
    public void deleteService(int id) {
        lmVideoCategoaryDao.deleteByPrimaryKey(id);
    }

    @Override
    public void updateService(LmVideoCategoary lmVideoCategoary) {
        lmVideoCategoaryDao.updateByPrimaryKey(lmVideoCategoary);
    }

    @Override
    public List<Map> findByCondient(Map<String, String> condient) {
        return lmVideoCategoaryDao.findByCondient(condient);
    }

    @Override
    public List<LmVideoCategoary> findAll() {
        return lmVideoCategoaryDao.selectAll();
    }

    @Override
    public List<LmVideoCategoary> findtopcategory() {
        return lmVideoCategoaryDao.findtopcategory();
    }

    @Override
    public List<Map<String, String>> findtopcategoryByApi() {
        return lmVideoCategoaryDao.findtopcategoryByApi();
    }
}
