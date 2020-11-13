package com.wink.livemall.member.service.impl;

import com.wink.livemall.member.dao.LmMemberAddressDao;
import com.wink.livemall.member.dto.LmMemberAddress;
import com.wink.livemall.member.service.LmMemberAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmMemberAddressServiceImpl implements LmMemberAddressService {

    @Resource
    private LmMemberAddressDao lmMemberAddressDao;

    @Override
    public LmMemberAddress findById(String id) {
        return lmMemberAddressDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMemberAddress> findAll() {
        return lmMemberAddressDao.selectAll();
    }

    @Override
    public void insertService(LmMemberAddress lmMemberAddress) {
        lmMemberAddressDao.insert(lmMemberAddress);
    }

    @Override
    public void updateService(LmMemberAddress lmMemberAddress) {
        lmMemberAddressDao.updateByPrimaryKey(lmMemberAddress);
    }

    @Override
    public void deleteService(int id) {
        lmMemberAddressDao.deleteByPrimaryKey(id);
    }


    @Override
    public List<LmMemberAddress> findByMemberid(int id) {
        return lmMemberAddressDao.findByMemberid(id);
    }

    @Override
    public List<Map<String, String>> findByMemberidByapi(int id) {
        return lmMemberAddressDao.findByMemberidByapi(id);
    }
}
