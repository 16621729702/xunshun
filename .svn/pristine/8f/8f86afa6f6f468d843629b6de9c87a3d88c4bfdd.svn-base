package com.wink.livemall.goods.service.impl;

import com.wink.livemall.goods.dao.LmGoodCommentDao;
import com.wink.livemall.goods.dto.LmGoodComment;
import com.wink.livemall.goods.service.LmGoodCommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmGoodCommentServiceImpl implements LmGoodCommentService {

    @Resource
    private LmGoodCommentDao lmGoodCommentDao;

    @Override
    public List<Map<String,Object>> findByCondient(Map<String, String> condient) {
        return lmGoodCommentDao.findByCondient(condient);
    }

    @Override
    public LmGoodComment findById(String id) {
        return lmGoodCommentDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void updateService(LmGoodComment lmGoodComment) {
        lmGoodCommentDao.updateByPrimaryKey(lmGoodComment);
    }

    @Override
    public void delete(String id) {
        lmGoodCommentDao.deleteByPrimaryKey(Integer.parseInt(id));
    }
}
