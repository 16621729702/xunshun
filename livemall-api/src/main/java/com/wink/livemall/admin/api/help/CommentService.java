package com.wink.livemall.admin.api.help;

import java.util.Map;

public interface CommentService {

    Map<String,Object> autoRefundFalsify(String falsifyId, String refundAmount) throws Exception;
}
