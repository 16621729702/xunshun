package com.wink.livemall.merch.service.impl;

import com.wink.livemall.merch.dao.LmMerchCategoryDao;
import com.wink.livemall.merch.dto.LmMerchCategory;
import com.wink.livemall.merch.service.LmMerchCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmMerchCategoryServiceImpl implements LmMerchCategoryService {

    @Resource
    private LmMerchCategoryDao lmMerchCategoryDao;

    @Override
    public List<LmMerchCategory> findActiceListByApi() {
        return lmMerchCategoryDao.findActiceListByApi();
    }


    
    
}
