package com.wink.livemall.merch.service;

import com.wink.livemall.merch.dao.LmBusinessEntityDao;
import com.wink.livemall.merch.dao.LmMerchInfoDao;
import com.wink.livemall.merch.dto.LmBusinessEntity;
import com.wink.livemall.merch.dto.LmMerchApplyInfo;
import com.wink.livemall.merch.dto.LmMerchInfo;

import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

public interface LmBusinessEntityService {
	
	
	/**
	 * 获取经营主体
	 * @param example
	 * @return
	 */
	List<LmBusinessEntity> findByCondient(Example example);
    

}
