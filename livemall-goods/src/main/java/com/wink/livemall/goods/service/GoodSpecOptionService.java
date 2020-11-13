package com.wink.livemall.goods.service;

import com.wink.livemall.goods.dto.GoodSpec;
import com.wink.livemall.goods.dto.GoodSpecOption;

import java.util.List;


public interface GoodSpecOptionService {



    void updateService(GoodSpecOption goodSpecOption);

    void insertService(GoodSpecOption goodSpecOption);

    List<GoodSpecOption> findByGoodid(String id);

    GoodSpecOption findbyId(String s);

    void deleteByGoodid(String id);
}