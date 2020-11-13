package com.wink.livemall.sys.setting.service;

import com.wink.livemall.sys.setting.dto.Lideshow;

import java.util.List;
import java.util.Map;

public interface LideshowService {
    /**
     * 根据主键查询
     * @param id
     * @return
     */
    Lideshow findById(String id);

    /**
     * 新增轮转图
     * @param lideshow
     */
    void insertService(Lideshow lideshow);

    /**
     * 更新信息
     * @param lideshow
     */
    void updateService(Lideshow lideshow);

    /**
     * 根据条件查询
     * @param condient
     * @return
     */
    List<Lideshow> findByCondient(Map<String, String> condient);


    /**
     * 获取首页轮播图
     * @param index
     * @return
     */
    List<Lideshow> findListBytype(int index);

    void deleteService(String id);
}
