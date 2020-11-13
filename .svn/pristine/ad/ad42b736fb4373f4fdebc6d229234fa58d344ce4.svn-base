package com.wink.livemall.sys.dict.service;

import com.wink.livemall.sys.dict.dto.LmSysDictItem;

import java.util.List;

public interface Dict_itemService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    LmSysDictItem findById(String id);

    /**
     * 查询所有
     * @return
     */
    List<LmSysDictItem> findAll();

    List<LmSysDictItem> findByDictCode(String code);

    void addService(LmSysDictItem lmSysDictItem);

    void updateService(LmSysDictItem lmSysDictItem);

    void deleteService(String id);
}
