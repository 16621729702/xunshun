package com.wink.livemall.sys.dict.service.impl;

import com.wink.livemall.sys.dict.dao.DictDao;
import com.wink.livemall.sys.dict.dto.LmSysDict;
import com.wink.livemall.sys.dict.service.DictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    @Resource
    private DictDao dictDao;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public LmSysDict findById(String id) {
        return dictDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    /**
     * 查询所有
     * @return
     */
    public List<LmSysDict> findAll(){
        return dictDao.selectAll();
    }

    @Override
    public void addService(LmSysDict lmSysDict) {
        dictDao.insert(lmSysDict);
    }

    @Override
    public void updateservice(LmSysDict lmSysDict) {
        dictDao.updateByPrimaryKey(lmSysDict);
    }

    @Override
    public void deleteService(String id) {
        dictDao.deleteByPrimaryKey(Integer.parseInt(id));
    }
}
