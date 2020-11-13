package com.wink.livemall.goods.service.impl;


import com.wink.livemall.goods.dao.GoodSpecDao;
import com.wink.livemall.goods.dao.GoodSpecItemDao;
import com.wink.livemall.goods.dao.GoodSpecOptionDao;
import com.wink.livemall.goods.dto.GoodSpec;
import com.wink.livemall.goods.dto.GoodSpecItem;
import com.wink.livemall.goods.dto.GoodSpecOption;
import com.wink.livemall.goods.service.GoodSpecOptionService;
import com.wink.livemall.goods.service.GoodSpecService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class GoodSpecOptionServiceImpl implements GoodSpecOptionService {

    @Resource
    private GoodSpecOptionDao goodSpecOptionDao;



    @Override
    public void updateService(GoodSpecOption goodSpecOption) {
        goodSpecOptionDao.updateByPrimaryKey(goodSpecOption);
    }

    @Override
    public void insertService(GoodSpecOption goodSpecOption) {
        goodSpecOptionDao.insert(goodSpecOption);
    }

    @Override
    public List<GoodSpecOption> findByGoodid(String id) {
        return goodSpecOptionDao.findByGoodid(Integer.parseInt(id));
    }

    @Override
    public GoodSpecOption findbyId(String id) {
        return goodSpecOptionDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void deleteByGoodid(String id) {
        goodSpecOptionDao.deleteByGoodid(Integer.parseInt(id));
    }
}
