package com.wink.livemall.help.service.impl;

import com.wink.livemall.help.dao.LmFeedbackDao;
import com.wink.livemall.help.dto.LmFeedback;
import com.wink.livemall.help.service.LmFeedbackService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmFeedbackServiceImpl implements LmFeedbackService {

    @Resource
    private LmFeedbackDao lmFeedbackDao;

    @Override
    public LmFeedback findById(String id) {
        return lmFeedbackDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmFeedback> findAll() {
        return lmFeedbackDao.selectAll();
    }

    @Override
    public void insertService(LmFeedback lmFeedback) {
        lmFeedbackDao.insert(lmFeedback);
    }

    @Override
    public void updateService(LmFeedback lmFeedback) {
        lmFeedbackDao.updateByPrimaryKey(lmFeedback);
    }

    @Override
    public void deleteService(String id) {
        lmFeedbackDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmFeedback> findByCondient(Map<String, String> condient) {
        return lmFeedbackDao.findByCondient(condient);
    }
}
