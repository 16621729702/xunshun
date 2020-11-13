package com.wink.livemall.goods.service.impl;

import com.wink.livemall.goods.dao.GoodDao;
import com.wink.livemall.goods.dao.GoodSpecDao;
import com.wink.livemall.goods.dao.GoodSpecItemDao;
import com.wink.livemall.goods.dao.MerchGoodDao;
import com.wink.livemall.goods.dao.MerchWareHouseDao;
import com.wink.livemall.goods.dto.WareHouse;
import com.wink.livemall.goods.service.MerchGoodService;
import com.wink.livemall.goods.service.MerchWareHouseService;

import org.springframework.stereotype.Service;


import javax.annotation.Resource;

import java.util.List;
import java.util.Map;


@Service
public class MerchWareHouseServiceImpl implements MerchWareHouseService {

    @Resource
    private MerchWareHouseDao merchWareHouseDao;
  
    /**
     * 查询库
     * @param condient
     * @return
     */
	@Override
	public List<WareHouse> findList() {
		// TODO Auto-generated method stub
		return merchWareHouseDao.findList();
	}


  


}
