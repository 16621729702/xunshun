package com.wink.livemall.member.service.impl;

import com.wink.livemall.member.dao.LmMemberFavDao;
import com.wink.livemall.member.dto.LmMemberFav;
import com.wink.livemall.member.service.LmMemberFavService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmMemberFavServiceImpl implements LmMemberFavService {

    @Resource
    private LmMemberFavDao lmMemberFavDao;

    @Override
    public LmMemberFav findById(String id) {
        return lmMemberFavDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMemberFav> findAll() {
        return lmMemberFavDao.selectAll();
    }

    @Override
    public void insertService(LmMemberFav lmMemberFav) {
        lmMemberFavDao.insert(lmMemberFav);
    }

    @Override
    public void updateService(LmMemberFav lmMemberFav) {
        lmMemberFavDao.updateByPrimaryKey(lmMemberFav);
    }

    @Override
    public void deleteService(int id) {
        lmMemberFavDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<LmMemberFav> findByMemberid(int parseInt) {
        return lmMemberFavDao.findByMemberid(parseInt);
    }

    @Override
    public List<Map<String, String>> findInfoByMemberid(int userid) {
        return lmMemberFavDao.findInfoByMemberid(userid);
    }

    @Override
    public LmMemberFav findByMemberidAndGoodid(String userid, String goodid) {
        return lmMemberFavDao.findByMemberidAndGoodid(Integer.parseInt(userid),Integer.parseInt(goodid));
    }

    @Override
    public LmMemberFav findByMemberidAndVideoidByApi(int menberid, int videoid) {
        return lmMemberFavDao.findByMemberidAndVideoidByApi(menberid,videoid);
    }

    @Override
    public void addService(LmMemberFav lmMemberFav) {
        lmMemberFavDao.insert(lmMemberFav);
    }

    @Override
    public List<Map<String, String>> findInfoByMemberidByApi(int userid, int type) {
        return lmMemberFavDao.findInfoByMemberidByApi(userid,type);
    }

    @Override
    public LmMemberFav findByMemberidAndTypeAndId(int userid, int type, int id) {
        return lmMemberFavDao.findByMemberidAndTypeAndId(userid,type,id);
    }
}
