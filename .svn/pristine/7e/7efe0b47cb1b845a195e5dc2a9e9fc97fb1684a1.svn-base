package com.wink.livemall.sys.setting.service;

import com.wink.livemall.sys.setting.dto.Express;

import java.util.List;
import java.util.Map;

public interface ExpressService {
    /**
     * 根据主键查询
     * @param id
     * @return
     */
    Express findById(String id);

    /**
     * 更新信息
     * @param express
     */
    void updateService(Express express);

    /**
     * 新增信息
     * @param express
     */
    void addService(Express express);

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    void deleteById(String id);

    /**
     * 根据条件查询
     * @param condient
     * @return
     */
    List<Express> findByCondient(Map<String, String> condient);

    List<Express> findActiveList();
}
