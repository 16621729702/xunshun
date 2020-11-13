package com.wink.livemall.sys.role.service.impl;

import com.wink.livemall.sys.permissionrole.dao.PermissionRoleDao;
import com.wink.livemall.sys.role.dao.RoleDao;
import com.wink.livemall.sys.role.dto.Role;
import com.wink.livemall.sys.role.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleDao roleDao;
    @Resource
    private PermissionRoleDao permissionRoleDao;

    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    /**
     * 查询所有角色
     * @return
     */
    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public void deleteRole(String id) {
        /**
         * 移除角色
         */
        roleDao.deleteRole(Integer.parseInt(id));
    }

    /**
     * 新增角色 和 角色权限关系
     * @param role
     * @param permissions
     */
    @Transactional
    @Override
    public void addRole(Role role, String permissions) {
        roleDao.addRole(role);
        String [] permissionids = permissions.split(",");
        for(String id:permissionids){
            permissionRoleDao.addPermissionrole(role.getId(),Integer.parseInt(id));
        }
    }

    /**
     * 修改角色信息和 权限角色关系
     * @param id
     * @param name
     * @param code
     * @param status
     * @param orderno
     * @param permissions
     */
    @Override
    public void updateRole(String id, String name, String code, String status, String orderno, String permissions) {
        //更新角色信息
        roleDao.updateRole(id,name,code,status,orderno);
        if(permissions!=null){
            String [] permissionids = permissions.split(",");
            //移除原有权限关系
            permissionRoleDao.deleteRolePermission(Integer.parseInt(id));
            //新增新的权限角色关系
            for(String permissoinid:permissionids){
                permissionRoleDao.addPermissionrole(Integer.parseInt(id),Integer.parseInt(permissoinid));
            }
        }
    }
}
