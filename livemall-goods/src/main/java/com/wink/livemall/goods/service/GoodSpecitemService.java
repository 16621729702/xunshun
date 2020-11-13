package com.wink.livemall.goods.service;

import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.GoodSpecItem;

import java.util.List;
import java.util.Map;


public interface GoodSpecitemService {

    /**
     * 根据goodid查询
     * @param id
     * @return
     */
    List<GoodSpecItem> findByGoodId(String id);

    /**
     * 删除
     * @param id
     */
    void delete(String id);

    /**
     * 添加
     * @param goodSpecItem
     */
    void insertGoodSpecItem(GoodSpecItem goodSpecItem);

    /**
     * 根据主键查询
     * @param s
     * @return
     */
    GoodSpecItem findById(String s);

    /**
     * 更新信息
     * @param goodSpecItem
     */
    void updateGoodSpecItem(GoodSpecItem goodSpecItem);

    List<GoodSpecItem> findBySpecid(int id);

    List<String> findtitleBySpecid(int id);
}