package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.LmFalsifyRefundReason;
import org.apache.ibatis.annotations.Param;

public interface LmFalsifyRefundReasonService {


    LmFalsifyRefundReason findRefundReason(int memberId,int goodId,int type);

    void  insert(LmFalsifyRefundReason lmFalsifyRefundReason);

    void  update(LmFalsifyRefundReason lmFalsifyRefundReason);

}
