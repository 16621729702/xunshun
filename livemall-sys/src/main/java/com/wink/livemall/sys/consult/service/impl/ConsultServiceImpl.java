package com.wink.livemall.sys.consult.service.impl;

import com.wink.livemall.sys.consult.dao.ConsultDao;
import com.wink.livemall.sys.consult.dto.Consult;
import com.wink.livemall.sys.consult.service.ConsultService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ConsultServiceImpl implements ConsultService {

    @Resource
    private ConsultDao consultDao;

    @Override
    public List<String> findActiveList() {
        return consultDao.findActiveList();
    }

    @Override
    public String findInfoByTitle(String title) {
        return consultDao.findInfoByTitle(title);
    }

    @Override
    public List<Consult> findAll() {
        return consultDao.selectAll();
    }

    @Override
    public Consult findById(String id) {
        return consultDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void addService(Consult consult) {
        consultDao.insert(consult);
    }

    @Override
    public void updateservice(Consult consult) {
        consultDao.updateByPrimaryKey(consult);
    }

    @Override
    public void deleteService(String id) {
        consultDao.deleteByPrimaryKey(Integer.parseInt(id));
    }


}
