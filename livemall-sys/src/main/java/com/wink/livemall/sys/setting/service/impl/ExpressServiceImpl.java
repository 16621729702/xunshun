package com.wink.livemall.sys.setting.service.impl;

import com.wink.livemall.sys.setting.dao.ExpressDao;
import com.wink.livemall.sys.setting.dto.Express;
import com.wink.livemall.sys.setting.service.ExpressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ExpressServiceImpl implements ExpressService {


    @Resource
    private ExpressDao expressDao;

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @Override
    public Express findById(String id) {
        return expressDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    /**
     * 更新信息
     * @param express
     */
    @Override
    public void updateService(Express express) {
        expressDao.updateByPrimaryKey(express);
    }

    /**
     * 新增信息
     * @param express
     */
    @Override
    public void addService(Express express) {
        expressDao.insert(express);
    }

    /**
     * 根据主键删除
     * @param id
     */
    @Override
    public void deleteById(String id) {
        expressDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    /**
     * 根据条件查询列表
     * @param condient
     * @return
     */
    @Override
    public List<Express> findByCondient(Map<String, String> condient) {
        return expressDao.findByCondient(condient);
    }

    @Override
    public List<Express> findActiveList() {
        return expressDao.findActiveList();
    }
}
