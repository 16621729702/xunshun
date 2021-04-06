package com.wink.livemall.order.service.impl;

import com.wink.livemall.order.dao.LmOrderGoodsDao;
import com.wink.livemall.order.dto.LmOrderComment;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.service.LmOrderGoodsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LmOrderGoodsServiceImpl implements LmOrderGoodsService {
    @Resource
    private LmOrderGoodsDao lmOrderGoodsDao;
    @Override
    public void insertService(LmOrderGoods lmOrderGoods) {
        lmOrderGoodsDao.insertSelective(lmOrderGoods);
    }

    @Override
    public LmOrderGoods findByOrderid(int id) {
        return lmOrderGoodsDao.findByOrderid(id);
    }

	@Override
	public LmOrderGoods findByGoodsid0(int id) {
		// TODO Auto-generated method stub
		return lmOrderGoodsDao.findByGoodsid0(id);
	}

    @Override
    public LmOrderGoods findByGoodsid1(int id) {
        // TODO Auto-generated method stub
        return lmOrderGoodsDao.findByGoodsid1(id);
    }

    @Override
    public List<LmOrderComment> findByMerchid(int merchid) {
        return lmOrderGoodsDao.findByMerchid(merchid);
    }

    @Override
    public List<LmOrderGoods> findByGoodRepeat(int goodid){

            return lmOrderGoodsDao.findByGoodRepeat(goodid);
    }

}
