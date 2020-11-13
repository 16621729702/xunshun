package com.wink.livemall.sys.setting.service.impl;

import com.wink.livemall.sys.setting.dao.TagDao;
import com.wink.livemall.sys.setting.dto.Tag;
import com.wink.livemall.sys.setting.service.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImp implements TagService {

    @Resource
    private TagDao tagDao;
    @Override
    public Tag findById(String id) {
        return tagDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void deleteById(String id) {
        tagDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void addService(Tag tag) {
        tagDao.insert(tag);
    }

    @Override
    public void updateService(Tag tag) {
        tagDao.updateByPrimaryKey(tag);
    }

    @Override
    public List<Tag> findByCondient(Map<String, String> condient) {
        return tagDao.findByCondient(condient);
    }
}
