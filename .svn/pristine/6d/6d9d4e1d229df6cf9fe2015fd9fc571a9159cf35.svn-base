package com.wink.livemall.goods.service.impl;

import com.wink.livemall.goods.dao.LmGoodAuctionDao;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.service.LmGoodAuctionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LmGoodAuctionServiceImpl implements LmGoodAuctionService {

    @Resource
    private LmGoodAuctionDao lmGoodAuctionDao;

    @Override
    public LmGoodAuction findnowPriceByGoodidByApi(int goodid,int type) {
        return lmGoodAuctionDao.findnowPriceByGoodidByApi(goodid,type);
    }

    @Override
    public List<LmGoodAuction> findAllByGoodid(String id,int type) {
        return lmGoodAuctionDao.findAllByGoodid(Integer.parseInt(id),type);
    }

    @Override
    public void deleteService(LmGoodAuction lm) {
        lmGoodAuctionDao.delete(lm);
    }
}
