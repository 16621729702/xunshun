package com.wink.livemall.sys.withdraw.service;

import com.wink.livemall.sys.withdraw.dto.Bank;

import java.util.List;
import java.util.Map;

public interface BankService {


    void insertBank(Bank bank);

    void updateBank(Bank bank);

    void delBank(int id);

    List<Map<String,Object>> findListByUserId(int user_id);

    Bank findById(Integer id);
}
