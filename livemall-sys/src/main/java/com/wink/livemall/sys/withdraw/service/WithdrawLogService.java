package com.wink.livemall.sys.withdraw.service;

import com.wink.livemall.sys.withdraw.dto.Bank;
import com.wink.livemall.sys.withdraw.dto.WithdrawLog;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface WithdrawLogService {


    void insertWithdrawLog(WithdrawLog withdrawLog) throws ParseException;

    void updateWithdrawLog(WithdrawLog withdrawLog);


    List<WithdrawLog> findListByUserId(int user_id);

    List<WithdrawLog> findListByState(int state);

    List<WithdrawLog> findListByStateAndUserId(int state, int user_id);

    List<Map<String,Object>> findListByLogId(int merId);

}
