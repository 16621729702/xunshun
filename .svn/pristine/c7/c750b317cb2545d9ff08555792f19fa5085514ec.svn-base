package com.wink.livemall.merch.service.impl;

import com.wink.livemall.merch.dao.LmMerchApplyInfoDao;
import com.wink.livemall.merch.dao.LmMerchInfoDao;
import com.wink.livemall.merch.dto.LmMerchApplyInfo;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.merch.service.LmMerchRegisterService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmMerchRegisterServiceImpl implements LmMerchRegisterService {

    @Resource
    private LmMerchInfoDao lmMerchInfoDao;
    @Resource
    private LmMerchApplyInfoDao lmMerchApplyInfoDao;




    @Override
    public int updateService(LmMerchInfo lmMerchInfo) {
      return  lmMerchInfoDao.updateByPrimaryKeySelective(lmMerchInfo);
    }




	@Override
	public int addMerchInfo(LmMerchInfo info) {
		return lmMerchInfoDao.insert(info);
	
	}




	@Override
	public LmMerchInfo findMerchInfoByid(int id) {
		// TODO Auto-generated method stub
		return lmMerchInfoDao.selectByPrimaryKey(id);
	}




	@Override
	public int addMerchInfoGetId(LmMerchInfo info) {
		// TODO Auto-generated method stub
		lmMerchInfoDao.insert(info);
		
		return 0;
	}

   
}
