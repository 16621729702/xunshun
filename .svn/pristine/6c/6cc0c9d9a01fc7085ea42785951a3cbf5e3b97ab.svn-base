package com.wink.livemall.sys.setting.service.impl;

import com.wink.livemall.sys.setting.dao.TagDao;
import com.wink.livemall.sys.setting.dao.TemplatesDao;
import com.wink.livemall.sys.setting.dto.Tag;
import com.wink.livemall.sys.setting.dto.Templates;
import com.wink.livemall.sys.setting.service.TagService;
import com.wink.livemall.sys.setting.service.TemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TemplateServiceImp implements TemplateService {

    @Resource
    private TemplatesDao templatesDao;

    @Override
    public Templates findById(String id) {
        return templatesDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void deleteById(String id) {
        templatesDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void addService(Templates templates) {
        templatesDao.insert(templates);
    }

    @Override
    public void updateService(Templates templates) {
        templatesDao.updateByPrimaryKey(templates);
    }

    @Override
    public List<Templates> findByCondient(Map<String, String> condient) {
        return templatesDao.findByCondient(condient);
    }
}
