package com.wink.livemall.video.service.impl;

import com.wink.livemall.video.dao.LmVideoCoreDao;
import com.wink.livemall.video.dto.LmVideoCore;
import com.wink.livemall.video.service.LmVideoCoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LmVideoCoreServiceImpl implements LmVideoCoreService {

    @Resource
    private LmVideoCoreDao lmVideoCoreDao;

    @Override
    public LmVideoCore findById(String id) {
        return lmVideoCoreDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public void addService(LmVideoCore lmVideoCore) {
        lmVideoCoreDao.insert(lmVideoCore);
    }

    @Override
    public void deleteService(int id) {
        lmVideoCoreDao.deleteByPrimaryKey(id);
    }

    @Override
    public void updateService(LmVideoCore lmVideoCore) {
        lmVideoCoreDao.updateByPrimaryKey(lmVideoCore);
    }

    @Override
    public List<Map> findByCondient(Map<String, String> condient) {
        return lmVideoCoreDao.findByCondient(condient);
    }

    /**
     * 获取置顶视频
     * @return
     */
    @Override
    public Map<String,String> findTopVideo() {
        List<Map<String,String>> list = lmVideoCoreDao.findTopVideo();
        if(list==null||list.size()==0){
            list = lmVideoCoreDao.findHotVideolist();
        }
        if(list!=null&&list.size()>0){
            return list.get(0);
        }else{
            return null;
        }

    }

    @Override
    public List<Map<String,String>> findHotVideolist() {
        return lmVideoCoreDao.findHotVideolist();
    }

    @Override
    public List<LmVideoCore> findByCategoryId(String category) {
        return lmVideoCoreDao.findByCategoryId(Integer.parseInt(category));
    }

    @Override
    public List<LmVideoCore> findByTag(String tag) {
        return lmVideoCoreDao.findByTag(tag);
    }

    @Override
    public List<Map<String, String>> findByCategoryIdByApi(String categoryid) {
        return lmVideoCoreDao.findByCategoryIdByApi(Integer.parseInt(categoryid));
    }

	@Override
	public void updateByFields(List<Map<String, String>> params, String id) {
		// TODO Auto-generated method stub
		lmVideoCoreDao.updateByFields(params, id);
	}

	@Override
	public List<LmVideoCore> findPage(Map<String, String> params) {
		// TODO Auto-generated method stub
		return lmVideoCoreDao.getpage(params);
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub
		lmVideoCoreDao.deleteByPrimaryKey(id);
	}
}
