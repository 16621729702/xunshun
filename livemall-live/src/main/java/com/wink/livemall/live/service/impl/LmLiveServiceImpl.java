package com.wink.livemall.live.service.impl;

import com.wink.livemall.live.dao.LmLiveDao;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.service.LmLiveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmLiveServiceImpl implements LmLiveService {

    @Resource
    private LmLiveDao lmLiveDao;

    @Override
    public List<Map<String,Object>> findListByCategoryIdByApi(int pid) {
        return lmLiveDao.findListByCategoryIdByApi(pid);
    }

    @Override
    public List<Map<String,Object>> findfollewLiveByApi(int memberid) {
        return lmLiveDao.findfollewLiveByApi(memberid);
    }

    @Override
    public Map<String, Object> finddirectlyinfoByApi() {
        return lmLiveDao.finddirectlyinfoByApi();
    }

    @Override
    public List<Map<String,Object>> findhotlive() {
        return lmLiveDao.findhotlive();
    }

    @Override
    public List<Map<String, String>> findsharefollewLiveByApi(int memberid) {
        return lmLiveDao.findsharefollewLiveByApi(memberid);
    }

    @Override
    public List<Map<String, String>> findshareListByCategoryIdByApi(int pid) {
        return lmLiveDao.findshareListByCategoryIdByApi(pid);
    }

    @Override
    public Map<String, Object> findsharehotlive() {
        return lmLiveDao.findsharehotlive();
    }

    @Override
    public Map<String, String> findRecommendLiveByapi() {
        return lmLiveDao.findRecommendLiveByapi();
    }

    @Override
    public List<Map<String,Object>> findHotLiveByApi() {
        return lmLiveDao.findHotLiveByApi();
    }

    @Override
    public Map<String, String> findShareRecommendLiveByapi() {
        return lmLiveDao.findShareRecommendLiveByapi();
    }

    @Override
    public List<Map<String, String>> findShareHotLiveByApi() {
        return lmLiveDao.findShareHotLiveByApi();
    }

    @Override
    public List<Map<String, Object>> findByCondient(Map<String, String> condient) {
        return lmLiveDao.findByCondient(condient);
    }

    @Override
    public void deleteService(int id) {
        lmLiveDao.deleteByPrimaryKey(id);
    }

    @Override
    public void insertService(LmLive lmLive) {
        lmLiveDao.insert(lmLive);
    }

    @Override
    public LmLive findbyId(String id) {
        return lmLiveDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void updateService(LmLive lmLive) {
        lmLiveDao.updateByPrimaryKeySelective(lmLive);
    }

    @Override
    public List<Map<String, String>> findListByNameByApi(String name) {
        return lmLiveDao.findListByNameByApi(name);
    }

    @Override
    public List<LmLive> findLiveedLive() {
        return lmLiveDao.findLiveedLive();
    }

    @Override
    public List<LmLive> findLiveList() {
        return lmLiveDao.findLiveList();
    }

    @Override
    public List<LmLive> findLivePreview() {
        return lmLiveDao.findLivePreview();
    }

    @Override
    public LmLive findByMerchid(int merchid) {
        return lmLiveDao.findLiveByMerchid(merchid);
    }

    @Override
    public List<LmLive> findHotLive() {
        return lmLiveDao.findHotLive();
    }
}
