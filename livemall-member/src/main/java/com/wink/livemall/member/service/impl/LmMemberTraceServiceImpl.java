package com.wink.livemall.member.service.impl;

import com.wink.livemall.member.dao.LmMemberTraceDao;
import com.wink.livemall.member.dto.LmMemberTrace;
import com.wink.livemall.member.service.LmMemberTraceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LmMemberTraceServiceImpl implements LmMemberTraceService {

    @Resource
    private LmMemberTraceDao lmMemberTraceDao;

    @Override
    public LmMemberTrace findById(String id) {
        return lmMemberTraceDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMemberTrace> findAll() {
        return lmMemberTraceDao.selectAll();
    }

    @Override
    public void insertService(LmMemberTrace lmMemberTrace) {
        lmMemberTraceDao.insert(lmMemberTrace);
    }

    @Override
    public void updateService(LmMemberTrace lmMemberTrace) {
        lmMemberTraceDao.updateByPrimaryKey(lmMemberTrace);
    }

    @Override
    public void deleteService(String id) {
        lmMemberTraceDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMemberTrace> findByMemberid(int memberid) {
        return lmMemberTraceDao.findByMemberid(memberid);
    }

    @Override
    public List<Map<String,Object>> findByMemberidAndType(int userid, int type) {
        List<Map<String,Object>> returnlist = new ArrayList<>();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(1==type){
            List<Map<String, Object>> byLive = lmMemberTraceDao.findByMemberidAndTypeLeftLive(userid, type);
            if(byLive!=null&&byLive.size()>0){
                for(Map<String, Object> map :byLive){
                    if(map.get("preview_time")!=null){
                        map.put("preview_time",dateFormat.format((Date)map.get("preview_time")));
                    }
                }
            }
            return byLive;
        }else if(2==type){
            return lmMemberTraceDao.findByMemberidAndTypeLeftGood(userid,type);
        }
        return returnlist;
    }

    @Override
    public LmMemberTrace findByMemberidAndGoodid(int userid, int goodid) {
        return lmMemberTraceDao.findByMemberidAndGoodid(userid,goodid);
    }

    @Override
    public LmMemberTrace findByMemberidAndLiveid(int userid, int liveid) {
        return lmMemberTraceDao.findByMemberidAndLiveid(userid,liveid);
    }
}
