package com.wink.livemall.sys.withdraw.service.impl;

import com.wink.livemall.sys.withdraw.dao.BankDao;
import com.wink.livemall.sys.withdraw.dto.Bank;
import com.wink.livemall.sys.withdraw.service.BankService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class BankServiceImpl implements BankService {

    @Resource
    private BankDao bankDao;


    @Override
    public void insertBank(Bank bank) {
        bank.setCreate_time(new Date());
        bank.setUpdate_time(new Date());
        bankDao.insertSelective(bank);
    }

    @Override
    public void updateBank(Bank bank) {
        bank.setUpdate_time(new Date());
        bankDao.updateByPrimaryKeySelective(bank);
    }

    @Override
    public void delBank(int id) {
        bankDao.deleteByPrimaryKey(id);
    }

    @Override
    public  List<Map<String,Object>> findListByUserId(int user_id) {
        return bankDao.findListByUserId(user_id);
    }

    @Override
    public Bank findById(Integer id) {
        return bankDao.findById(id);
    }


}
