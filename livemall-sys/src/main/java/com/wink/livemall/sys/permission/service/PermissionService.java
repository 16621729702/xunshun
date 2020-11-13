package com.wink.livemall.sys.permission.service;

import com.wink.livemall.sys.permission.dto.Permission;

import java.util.List;

public interface PermissionService {
    /**
     * 根据角色查询所有权限
     * @param roleid
     * @return
     */
    List<Permission> findListByRoleid(int roleid);

    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findAll();

    /**
     * 查询所有顶级菜单
     * @return
     */
    List<Permission> findListByPid();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Permission findPermissionById(int id);

    /**
     * 删除权限项
     * @param id
     */
    void deletepermission(int id);

    /**
     * 新增权限项
     * @param pid
     * @param name
     * @param code
     * @param order
     * @param status
     */
    void insertPermission(String pid, String name, String code, String order, String status);

    /**
     * 修改权限项
     * @param id
     * @param pid
     * @param name
     * @param code
     * @param order
     * @param status
     */
    void editPermission(String id, String pid, String name, String code, String order, String status);
}
