package com.wink.livemall.sys.admin.service;

import com.wink.livemall.sys.admin.dto.Admin;

import java.util.List;
import java.util.Map;

public interface AdminService {
    /**
     * 根据用户名查询管理用户
     * @param username
     * @return
     */
    Admin findAdminbyUsername(String username);

    /**
     * 根据参数动态查询用户列表
     * @param condient
     * @return
     */
    List<Admin> findListByCondient(Map<String, String> condient);

    /**
     * 新增后台用户
     * @param name
     * @param username
     * @param password
     * @param active
     * @param roles
     */
    void insertAdmin(String name, String username, String password, String active,String roles);

    /**
     * 删除角色信息
     * @param id
     */
    void deleteAdmin(String id);

    /**
     * 修改用户信息
     * @param name
     * @param username
     * @param password
     * @param status
     */
    void editAdmin(String id,String name, String username, String password, String status,String roles);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    Admin findAdminById(int id);
}
