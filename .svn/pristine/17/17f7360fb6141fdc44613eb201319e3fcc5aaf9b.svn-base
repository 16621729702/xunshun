package com.wink.livemall.member.service.impl;

import com.wink.livemall.member.dao.LmMemberDao;
import com.wink.livemall.member.dao.LmMemberLogDao;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberLog;
import com.wink.livemall.member.service.LmMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LmMemberServiceImpl implements LmMemberService {

    @Resource
    private LmMemberDao lmMemberDao;
    
    @Resource
    private LmMemberLogDao lmMemberLogDao;

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    @Override
    public LmMember findById(String id) {
        return lmMemberDao.selectByPrimaryKey(Integer.parseInt(id));
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<LmMember> findAll() {
        return lmMemberDao.selectAll();
    }


    @Override
    public void insertService(LmMember lmMember) {
        lmMemberDao.insertSelective(lmMember);
    }

    @Override
    public void updateService(LmMember lmMember) {
        lmMemberDao.updateByPrimaryKey(lmMember);
    }

    @Override
    public void deleteService(String id) {
        lmMemberDao.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public List<LmMember> findByCondient(Map<String, String> condient) {
        return lmMemberDao.findByCondient(condient);
    }

    @Override
    public LmMember findByMobile(String mobile) {
        return lmMemberDao.findByMobile(mobile);
    }

	@Override
	public void addMemberLog(LmMemberLog entity) {
		// TODO Auto-generated method stub
		lmMemberLogDao.insertSelective(entity);
	}
}
