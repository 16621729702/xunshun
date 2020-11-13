package com.wink.livemall.sys.permission.service.impl;

import com.wink.livemall.sys.permission.dao.PermissionDao;
import com.wink.livemall.sys.permission.dto.Permission;
import com.wink.livemall.sys.permission.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionDao permissionDao;

    /**
     * 根据角色id查询所有权限项
     * @param roleid
     * @return
     */
    @Override
    public List<Permission> findListByRoleid(int roleid) {
        return permissionDao.findPermissionsByRoleId(roleid);
    }

    /**
     * 查询所有权限
     * @return
     */
    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }

    /**
     * 查询所有顶级菜单
     * @return
     */
    @Override
    public List<Permission> findListByPid() {
        return permissionDao.findListByPid(0);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Permission findPermissionById(int id) {
        return permissionDao.findPermissionById(id);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void deletepermission(int id) {
        permissionDao.deletePermission(id);
    }

    @Override
    public void insertPermission(String pid, String name, String code, String orderno, String status) {
        permissionDao.insertPermission(Integer.parseInt(pid),name,code,Integer.parseInt(orderno),status);
    }

    @Override
    public void editPermission(String id, String pid, String name, String code, String orderno, String status) {
        permissionDao.editPermission(id,pid,name,code,orderno,status);
    }
}
