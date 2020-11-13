package com.wink.livemall.goods.service;

import com.wink.livemall.goods.dto.LmGoodComment;

import java.util.List;
import java.util.Map;

public interface LmGoodCommentService {

    List<Map<String,Object>> findByCondient(Map<String, String> condient);

    LmGoodComment findById(String id);

    void updateService(LmGoodComment lmGoodComment);

    void delete(String id);
}
