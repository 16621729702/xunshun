package com.wink.livemall.sys.admin.service.impl;

import com.wink.livemall.sys.admin.dao.AdminDao;
import com.wink.livemall.sys.admin.dto.Admin;
import com.wink.livemall.sys.admin.service.AdminService;
import com.wink.livemall.sys.adminrole.dao.AdminRoleDao;
import com.wink.livemall.sys.adminrole.dto.AdminRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceimpl implements AdminService {

    @Resource
    private AdminDao adminDao;
    @Resource
    private AdminRoleDao adminRoleDao;

    /**
     * 根据用户名查询后台用户
     * @param username
     * @return
     */
    @Override
    public Admin findAdminbyUsername(String username) {
        return adminDao.findAdminbyUsername(username);
    }
    /**
     * 根据参数动态查询用户列表
     * @param condient
     * @return
     */
    @Override
    public List<Admin> findListByCondient(Map<String, String> condient) {
        return adminDao.findListByCondient(condient);
    }

    /**
     * 新增后台用户
     * @param name
     * @param username
     * @param password
     * @param active
     * @param roles
     */
    @Override
    @Transactional
    public void insertAdmin(String name, String username, String password, String active,String roles) {
        //新增用户
        Admin admin = new Admin();
        admin.setPassword(password);
        admin.setName(name);
        admin.setStatus(active);
        admin.setUsername(username);
        adminDao.insert(admin);
        //新增用户和角色关系
        String [] roleids = roles.split(",");
        for(String id:roleids){
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminid(admin.getId());
            adminRole.setRoleid(Integer.parseInt(id));
            adminRoleDao.insert(adminRole);
        }
    }

    /**
     * 删除用户信息
     * @param id
     */
    @Override
    public void deleteAdmin(String id) {
        adminDao.deleteAdmin(Integer.parseInt(id));
    }

    /**
     * 修改用户信息
     * @param id
     * @param name
     * @param username
     * @param password
     * @param status
     */
    @Override
    @Transactional
    public void editAdmin(String id,String name, String username, String password, String status,String roles) {
        //更新用户信息
        Admin admin = adminDao.findAdminById(Integer.parseInt(id));
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setName(name);
        admin.setStatus(status);
        adminDao.updateByPrimaryKey(admin);
        //更新用户角色关系
        if(roles!=null){
            //删除原有关系
            adminRoleDao.deleteByAdminId(Integer.parseInt(id));
            //创建新的关系
            String [] roleids = roles.split(",");
            if(roleids.length>1){
                for(String roleid:roleids){
                    AdminRole adminRole= new AdminRole();
                    adminRole.setAdminid(Integer.parseInt(id));
                    adminRole.setRoleid(Integer.parseInt(roleid));
                    adminRoleDao.insert(adminRole);
                }
            }else{
                AdminRole adminRole= new AdminRole();
                adminRole.setAdminid(Integer.parseInt(id));
                adminRole.setRoleid(Integer.parseInt(roles));
                adminRoleDao.insert(adminRole);
            }
        }
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @Override
    public Admin findAdminById(int id) {
        return adminDao.findAdminById(id);
    }
}
