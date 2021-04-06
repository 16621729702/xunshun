package com.wink.livemall.member.service;

import com.wink.livemall.member.dto.CommissionLog;

import java.util.List;
import java.util.Map;

public interface CommissionLogService {

    void insertCommissionLog(CommissionLog commissionLog);

    void updateCommissionLog(CommissionLog commissionLog);

    List<CommissionLog>  findListByGainer(int gainer);

    List<Map<String, Object>> findListCredit(Integer userId);

    List<Map<String, Object>> userCommissionLog(Integer userId);
}
