package com.wink.livemall.sys.dict.service.impl;

import com.wink.livemall.sys.dict.dao.Dict_itemDao;
import com.wink.livemall.sys.dict.dto.LmSysDictItem;
import com.wink.livemall.sys.dict.service.Dict_itemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class Dict_itemServiceImpl implements Dict_itemService {

    @Resource
    private Dict_itemDao dict_itemDao;


    /**
     * 根据id查询
     */
    public LmSysDictItem findById(String id){
        return dict_itemDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    /**
     * 查询所有
     */
    public List<LmSysDictItem> findAll(){
        return dict_itemDao.selectAll();
    }

    @Override
    public List<LmSysDictItem> findByDictCode(String code) {
        return dict_itemDao.findByDictCode(code);
    }

    @Override
    public void addService(LmSysDictItem lmSysDictItem) {
        dict_itemDao.insert(lmSysDictItem);
    }

    @Override
    public void updateService(LmSysDictItem lmSysDictItem) {
        dict_itemDao.updateByPrimaryKey(lmSysDictItem);
    }

    @Override
    public void deleteService(String id) {
        dict_itemDao.deleteByPrimaryKey(Integer.parseInt(id));
    }
}
