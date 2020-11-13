package com.wink.livemall.merch.service;

import com.wink.livemall.merch.dto.LmMerchCategory;

import java.util.List;
import java.util.Map;

/**
 * 商户分类信息
 */
public interface LmMerchCategoryService {


    /*-------------------------app接口-----------------------------------*/
    //获取显示的商户分类
    List<LmMerchCategory> findActiceListByApi();

}
