package com.wink.livemall.order.service;

import com.wink.livemall.order.dto.LmOrderComment;

import java.util.List;
import java.util.Map;

public interface LmOrderCommentService {
    void insertService(LmOrderComment lmOrderComment);

    List<Map<String, Object>> findByMerchId(String merchid);
}
