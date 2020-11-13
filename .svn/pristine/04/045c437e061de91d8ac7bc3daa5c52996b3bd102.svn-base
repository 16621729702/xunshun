package com.wink.livemall.order.service.impl;

import com.wink.livemall.order.dao.LmOrderGoodsDao;
import com.wink.livemall.order.dto.LmOrderComment;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.service.LmOrderGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LmOrderGoodsServiceImpl implements LmOrderGoodsService {
    @Resource
    private LmOrderGoodsDao lmOrderGoodsDao;
    @Override
    public void insertService(LmOrderGoods lmOrderGoods) {
        lmOrderGoodsDao.insert(lmOrderGoods);
    }

    @Override
    public LmOrderGoods findByOrderid(int id) {
        return lmOrderGoodsDao.findByOrderid(id);
    }

	@Override
	public LmOrderGoods findByGoodsid(int id) {
		// TODO Auto-generated method stub
		return lmOrderGoodsDao.findByGoodsid(id);
	}

    @Override
    public List<LmOrderComment> findByMerchid(int merchid) {
        return lmOrderGoodsDao.findByMerchid(merchid);
    }
}
