package com.wink.livemall.help.service.impl;

import com.wink.livemall.help.dao.LmHelpCategoryDao;
import com.wink.livemall.help.dto.LmHelpCategory;
import com.wink.livemall.help.service.LmHelpCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmHelpCategoryServiceImpl implements LmHelpCategoryService {

    @Resource
    private LmHelpCategoryDao lmHelpCategoryDao;

    @Override
    public LmHelpCategory findById(String id) {
        return lmHelpCategoryDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmHelpCategory> findAll() {
        return lmHelpCategoryDao.selectAll();
    }

    @Override
    public void insertService(LmHelpCategory lmHelpCategory) {
        lmHelpCategoryDao.insert(lmHelpCategory);
    }

    @Override
    public void updateService(LmHelpCategory lmHelpCategory) {
        lmHelpCategoryDao.updateByPrimaryKey(lmHelpCategory);
    }

    @Override
    public void deleteService(String id) {
        lmHelpCategoryDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<Map<String, String>> findTopByApi() {
        return lmHelpCategoryDao.findTopByApi();
    }

    @Override
    public List<Map<String, String>> findByPidByApi(String categoryid) {
        return lmHelpCategoryDao.findByPidByApi(Integer.parseInt(categoryid));
    }
}
