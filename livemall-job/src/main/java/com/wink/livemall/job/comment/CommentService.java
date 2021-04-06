package com.wink.livemall.job.comment;

import java.util.Map;

public interface CommentService {

    Map<String,Object> autoRefundFalsify(String falsifyId, String refundAmount) throws Exception;
}
