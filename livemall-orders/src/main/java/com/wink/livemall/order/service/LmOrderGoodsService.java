package com.wink.livemall.order.service;

import com.wink.livemall.order.dto.LmOrderComment;
import com.wink.livemall.order.dto.LmOrderGoods;

import java.util.List;

public interface LmOrderGoodsService {
    void insertService(LmOrderGoods lmOrderGoods);

    LmOrderGoods findByOrderid(int id);
    
    LmOrderGoods findByGoodsid0(int id);

    LmOrderGoods findByGoodsid1(int id);

    List<LmOrderComment> findByMerchid(int merchid);

    List<LmOrderGoods> findByGoodRepeat(int goodid);
}
