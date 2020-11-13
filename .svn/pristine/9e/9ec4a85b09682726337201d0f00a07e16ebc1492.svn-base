package com.wink.livemall.sys.adminrole.service.impl;

import com.wink.livemall.sys.adminrole.dao.AdminRoleDao;
import com.wink.livemall.sys.adminrole.service.AdminRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class AdminRoleServiceImpl implements AdminRoleService {
    @Resource
    private AdminRoleDao adminRoleDao;

    /**
     * 根据用户id获取角色ids
     * @param id
     * @return
     */
    @Override
    public List<Integer> findRoleListByAdminId(int id) {
        return adminRoleDao.findRoleListByAdminId(id);
    }
}
