package com.wink.livemall.sys.msg.service.impl;

import com.wink.livemall.sys.msg.dao.LmPushmsgDao;
import com.wink.livemall.sys.msg.dto.LmPushmsg;
import com.wink.livemall.sys.msg.service.PushmsgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PushmsgServiceImpl implements PushmsgService {

    @Resource
    private LmPushmsgDao lmPushmsgDao;

    @Override
    @Transactional
    public void send(int sendid, String content, String type, int receiveid,int ismerch) {
        LmPushmsg pushmsg = new LmPushmsg();
        pushmsg.setContent(content);
        pushmsg.setCreatetime(new Date());
        pushmsg.setSendid(sendid);
        pushmsg.setType(type);
        pushmsg.setRecevieid(receiveid);
        pushmsg.setIsread(0);
        pushmsg.setIsmerch(ismerch);
        lmPushmsgDao.insert(pushmsg);

    }


	@Override
	public List<Map<String, Object>> findPageMerch(Map<String, String> params) {
		// TODO Auto-generated method stub
		return lmPushmsgDao.getPageMerch(params);
	}

    @Override
    public List<Map<String, Object>> getlistByreceiveid(int receiveid) {
        return lmPushmsgDao.getlistByreceiveid(receiveid);
    }

    @Override
    public List<Map<String, Object>> getlistBysendid(int sendid) {
        return lmPushmsgDao.getlistBysendid(sendid);
    }

    @Override
    public List<Map<String, Object>> getlistBysendidAndreceiveid(int sendid, int receiveid) {
        return lmPushmsgDao.getlistBysendidAndreceiveid(sendid,receiveid);
    }

    @Override
    public void deleteservice(int id) {
        lmPushmsgDao.deleteByPrimaryKey(id);
    }


}
