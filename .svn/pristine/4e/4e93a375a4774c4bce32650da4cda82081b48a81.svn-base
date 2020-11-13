package com.wink.livemall.order.service.impl;

import com.wink.livemall.order.dao.LmExpressDao;
import com.wink.livemall.order.dao.LmOrderExpressDao;

import com.wink.livemall.order.dto.LmExpress;
import com.wink.livemall.order.dto.LmOrderExpress;
import com.wink.livemall.order.service.LmOrderExpressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

@Service
public class LmOrderExpressServiceImpl implements LmOrderExpressService {

    @Resource
    private LmOrderExpressDao lmOrderExpressDao;
    
    @Resource
    private LmExpressDao lmExpressDao;

    @Override
    public Map<String, Object> findByOrderid(String orderid) {
        return lmOrderExpressDao.findByOrderid(orderid);
    }

    @Override
    public LmOrderExpress findByPKey(String id) {
        return lmOrderExpressDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void updateService(LmOrderExpress lmOrderExpress) {
        lmOrderExpressDao.updateByPrimaryKey(lmOrderExpress);
    }

    @Override
    public void insertService(LmOrderExpress lmOrderExpress) {
        lmOrderExpressDao.insert(lmOrderExpress);
    }

	@Override
	public List<Map<String, Object>> findList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LmExpress> getAll() {
		// TODO Auto-generated method stub
		return lmExpressDao.getAll();
	}
}
