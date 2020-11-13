package com.wink.livemall.goods.service;

import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.GoodSpec;

import java.util.List;
import java.util.Map;


public interface GoodSpecService {

    /**
     * 根据商品id查询商品规格
     * @param id
     * @return
     */
    List<GoodSpec> findbyGoodId(String id);
    /**
     * 根据id删除
     * @param id
     * @return
     */
    void delete(String id);

    /**
     * 添加商品规格
     * @param goodSpec
     */
    void insertGoodspec(GoodSpec goodSpec);

    /**
     * 根据主键查询
     * @param spec_id
     * @return
     */
    GoodSpec findByid(String spec_id);

    /**
     * 更新信息
     * @param goodSpec
     */
    void updateGoodspec(GoodSpec goodSpec);
}