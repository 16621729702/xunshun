package com.wink.livemall.order.service.impl;

import com.wink.livemall.order.dao.LmExpressDao;
import com.wink.livemall.order.dto.LmExpress;
import com.wink.livemall.order.service.LmExpressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LmExpressServiceImpl implements LmExpressService {
    @Resource
    private LmExpressDao lmExpressDao;

    @Override
    public List<LmExpress> findActiveList() {
        return lmExpressDao.getAll();
    }
}
