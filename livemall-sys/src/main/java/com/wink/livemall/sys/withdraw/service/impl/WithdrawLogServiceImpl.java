package com.wink.livemall.sys.withdraw.service.impl;


import com.wink.livemall.sys.withdraw.dao.WithdrawLogDao;
import com.wink.livemall.sys.withdraw.dto.WithdrawLog;
import com.wink.livemall.sys.withdraw.service.WithdrawLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class WithdrawLogServiceImpl implements WithdrawLogService {


    @Resource
    private WithdrawLogDao withdrawLogDao;

    @Override
    public void insertWithdrawLog(WithdrawLog withdrawLog) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String newDate = format.format(new Date());
        withdrawLogDao.insertWithdrawLog(withdrawLog,newDate);
    }

    @Override
    public void updateWithdrawLog(WithdrawLog withdrawLog) {
        withdrawLog.setUpdate_time(new Date());
        withdrawLogDao.updateByPrimaryKeySelective(withdrawLog);
    }

    @Override
    public List<WithdrawLog> findListByUserId(int user_id) {
        return withdrawLogDao.findListByUserId(user_id);
    }

    @Override
    public List<WithdrawLog> findListByState(int state) {
        return withdrawLogDao.findListByState(state);
    }

    @Override
    public List<WithdrawLog> findListByStateAndUserId(int state, int user_id) {
        return withdrawLogDao.findListByStateAndUserId(state,user_id);
    }

    @Override
    public List<Map<String, Object>> findListByLogId(int merId) {
        List<WithdrawLog> listByLogId = withdrawLogDao.findListByLogId(merId);
        List<Map<String, Object>> List=new LinkedList<>();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(listByLogId.size()>0){
            for(WithdrawLog withdrawLog:listByLogId){
                Map<String,Object> map=new HashMap<>();
                BigDecimal realPrice = withdrawLog.getAmount();
                map.put("price",realPrice);
                map.put("time",dateFormat.format(withdrawLog.getCreate_time()));
                map.put("type",withdrawLog.getState());
                map.put("description",withdrawLog.getDesc());
                List.add(map);
            }
        }
        return List;
    }

}
