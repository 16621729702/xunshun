package com.wink.livemall.order.service.impl;

import com.wink.livemall.order.dao.LmOrderCommentDao;
import com.wink.livemall.order.dto.LmOrderComment;
import com.wink.livemall.order.service.LmOrderCommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmOrderCommentServiceImpl implements LmOrderCommentService {
    @Resource
    private LmOrderCommentDao lmOrderCommentDao;
    @Override
    public void insertService(LmOrderComment lmOrderComment) {
        lmOrderCommentDao.insert(lmOrderComment);
    }

    @Override
    public List<Map<String, Object>> findByMerchId(String merchid) {
        return lmOrderCommentDao.findByMerchId(Integer.parseInt(merchid));
    }
}
