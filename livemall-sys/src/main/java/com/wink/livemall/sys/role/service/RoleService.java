package com.wink.livemall.sys.role.service;

import com.wink.livemall.sys.role.dto.Role;

import java.util.List;

public interface RoleService {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Role findById(Integer id);

    List<Role> findAll();

    /**
     * 移除角色 和 角色权限对应关系
     * @param id
     */
    void deleteRole(String id);

    /**
     * 新增角色 和 角色权限列表
     * @param role
     * @param permissions
     */
    void addRole(Role role, String permissions);

    /**
     * 修改角色信息和 权限角色关系
     * @param id
     * @param name
     * @param code
     * @param status
     * @param orderno
     * @param permissions
     */
    void updateRole(String id, String name, String code, String status, String orderno, String permissions);
}
