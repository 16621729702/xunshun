package com.wink.livemall.member.service.impl;

import com.wink.livemall.member.dao.LmMemberFollowDao;
import com.wink.livemall.member.dto.LmMemberFollow;
import com.wink.livemall.member.service.LmMemberFollowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmMemberFollowServiceImpl implements LmMemberFollowService {

    @Resource
    private LmMemberFollowDao lmMemberFollowDao;

    @Override
    public LmMemberFollow findById(String id) {
        return lmMemberFollowDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMemberFollow> findAll() {
        return lmMemberFollowDao.selectAll();
    }

    @Override
    public void insertService(LmMemberFollow lmMemberFollow) {
        lmMemberFollowDao.insert(lmMemberFollow);
    }

    @Override
    public void updateService(LmMemberFollow lmMemberFollow) {
        lmMemberFollowDao.updateByPrimaryKey(lmMemberFollow);
    }

    @Override
    public void deleteService(int id) {
        lmMemberFollowDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<LmMemberFollow> findByMemberid(int memberid) {
        return lmMemberFollowDao.findByMemberid(memberid);
    }

    @Override
    public List<Map<String, Object>> findByMemberidAndType(int userid, String type) {
        if("0".equals(type)){
            return lmMemberFollowDao.findLiveinfofoByUserid(userid);
        }
        if("1".equals(type)){
            return lmMemberFollowDao.findMerchInfoByUserid(userid);
        }
        return null;
    }



	@Override
	public List<Map<String, Object>> findlist(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return lmMemberFollowDao.getpage(params);
	}

    @Override
    public void addService(LmMemberFollow lmMemberFollow) {
        lmMemberFollowDao.insert(lmMemberFollow);
    }

    @Override
    public LmMemberFollow findByMemberidAndMerchId(String userid, int merchid) {
        return lmMemberFollowDao.findByMemberidAndMerchId(Integer.parseInt(userid),merchid);
    }

    @Override
    public LmMemberFollow findByMemberidAndTypeAndId(int userid, int type, int id) {
        return lmMemberFollowDao.findByMemberidAndTypeAndId(userid,type,id);
    }

	@Override
	public int  countNumYed(String merchid) {
		// TODO Auto-generated method stub
		return lmMemberFollowDao.countNumYed(merchid);
	}

    @Override
    public List<LmMemberFollow> findByMerchidAndType(int i, int id) {
        return lmMemberFollowDao.findByMerchidAndType(i,id);
    }

    @Override
    public List<LmMemberFollow> findByMerchidCount(int i, int id, int userid) {
        return lmMemberFollowDao.findByMerchidCount(i,id,userid);
    }

}
