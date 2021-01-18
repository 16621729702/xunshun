package com.wink.livemall.goods.service;

import com.wink.livemall.goods.dto.LmGoodAuction;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;


public interface LmGoodAuctionService {

    LmGoodAuction findnowPriceByGoodidByApi(int goodid,int type);

    List<LmGoodAuction> findAllByGoodid(String id,int type);

    void deleteService(LmGoodAuction lm);

    LmGoodAuction isHavingAuction(String memberid);
}
