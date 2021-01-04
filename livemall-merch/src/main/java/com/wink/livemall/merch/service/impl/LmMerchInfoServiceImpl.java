package com.wink.livemall.merch.service.impl;

import com.google.gson.Gson;
import com.wink.livemall.merch.dao.LmMerchAdminDao;
import com.wink.livemall.merch.dao.LmMerchApplyInfoDao;
import com.wink.livemall.merch.dao.LmMerchInfoDao;
import com.wink.livemall.merch.dto.LmMerchAdmin;
import com.wink.livemall.merch.dto.LmMerchApplyInfo;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

@Service
public class LmMerchInfoServiceImpl implements LmMerchInfoService {

    @Resource
    private LmMerchInfoDao lmMerchInfoDao;
    @Resource
    private LmMerchApplyInfoDao lmMerchApplyInfoDao;
    @Resource
    private LmMerchAdminDao lmMerchAdminDao;

    @Override
    public LmMerchInfo findById(String id) {
        return lmMerchInfoDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMerchInfo> findAll() {
        return lmMerchInfoDao.selectAll();
    }

    @Override
    public void insertService(LmMerchInfo lmMerchInfo) {
        lmMerchInfoDao.insert(lmMerchInfo);
    }

    @Override
    public int updateService(LmMerchInfo lmMerchInfo) {
      return  lmMerchInfoDao.updateByPrimaryKeySelective(lmMerchInfo);
    }

    @Override
    public void deleteService(String id) {
        lmMerchInfoDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    @Transactional
    public void deleteMerch(String id) {
        lmMerchInfoDao.deleteByPrimaryKey(Integer.parseInt(id));
        lmMerchApplyInfoDao.deleteByMerchid(Integer.parseInt(id));
    }

    @Override
    @Transactional
    public void check(String id, String state) {
        LmMerchApplyInfo lmMerchApplyInfo = lmMerchApplyInfoDao.selectByPrimaryKey(Integer.parseInt(id));
        LmMerchInfo lmMerchInfo = lmMerchInfoDao.selectByPrimaryKey(lmMerchApplyInfo.getMerchId());
        lmMerchApplyInfo.setApplyState(Integer.parseInt(state));
        lmMerchInfo.setState(Integer.parseInt(state));
        lmMerchInfoDao.updateByPrimaryKey(lmMerchInfo);
        lmMerchApplyInfoDao.updateByPrimaryKey(lmMerchApplyInfo);
    }

    @Override
    public List<LmMerchInfo> findByCondient(Map<String, String> condient) {
        return lmMerchInfoDao.findByCondient(condient);
    }
    @Override
    public List<Map<String, Object>> findByCondient2(Map<String, String> condient) {
        return lmMerchInfoDao.findByCondient2(condient);
    }
    @Override
    public List<Map<String, Object>> findMerchInfoByCategoryByApi(int categoryid) {
        return lmMerchInfoDao.findMerchInfoByCategoryByApi(categoryid);
    }

    @Override
    public Map<String, String> findMerchinfoByMerchidByApi(int merchid) {
        return lmMerchInfoDao.findMerchinfoByMerchidByApi(merchid);
    }

    @Override
    public Map<String, Object> findInfoByIdByApd(int id) {
        return lmMerchInfoDao.findInfoByIdByApd(id);
    }

    @Override
    public  List<LmMerchInfo>  findByMemberid(int memberid) {
        return lmMerchInfoDao.findByMemberid(memberid);
    }

    @Override
    public LmMerchInfo findDirectMerch() {
        return lmMerchInfoDao.findDirectMerch();
    }

    @Override
    public List<Map<String, Object>> findMerchInfoByNameByApi(String name) {
        return lmMerchInfoDao.findMerchInfoByNameByApi(name);
    }

    @Override
    public List<LmMerchInfo> findActiveMerch() {
        return lmMerchInfoDao.findActiveMerch();
    }

    @Override
    public int findmaxno() {
        return lmMerchInfoDao.findmaxno();
    }

    @Override
    public void addMerchAdmin(LmMerchAdmin lmMerchAdmin) {
        lmMerchAdminDao.insert(lmMerchAdmin);
    }

    @Override
    @Transactional
    public boolean deleteMerchAdmin(String merchid, String memberid) {
        boolean flag = false;
        LmMerchAdmin lmMerchAdmin = lmMerchAdminDao.findMerchAdmin(Integer.parseInt(memberid),Integer.parseInt(merchid));
        if(lmMerchAdmin!=null){
            lmMerchAdminDao.delete(lmMerchAdmin);
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Map<String, Object>> findAdminInfo(String merchid) {
        return lmMerchAdminDao.findAdminInfo(Integer.parseInt(merchid));
    }

    @Override
    public List<LmMerchAdmin> findAdminByMember(int id) {
        return lmMerchAdminDao.findAdminByMember(id);
    }


    @Override
	public int checkMerchEnable(String id) {
		// TODO Auto-generated method stub
    	
		return lmMerchInfoDao.checkMerchEnable(id);
	}

	@Override
	public void updateByFields(List<Map<String, String>> params, String id) {
		// TODO Auto-generated method stub
		lmMerchInfoDao.updateByFields(params, id);
	}

    @Override
    public LmMerchAdmin findMerchAdmin(int memberid, int merchid) {
        return lmMerchAdminDao.findMerchAdmin(memberid,merchid);
    }

    @Override
    public List<LmMerchAdmin> findAdminByMerchid(int merchid) {
        return lmMerchAdminDao.findAdminByMerchid(merchid);
    }


}
