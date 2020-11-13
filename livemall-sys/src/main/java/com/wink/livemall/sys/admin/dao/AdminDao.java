package com.wink.livemall.sys.admin.dao;

import com.wink.livemall.sys.admin.dto.Admin;
import org.apache.ibatis.annotations.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminDao extends tk.mybatis.mapper.common.Mapper<Admin>{

    /**
     * 通过用户名查询管理用户
     */
    @Select("SELECT * FROM lm_sys_admin WHERE username = #{username}")
    Admin findAdminbyUsername(@Param("username") String username);



    /**
     * 根据 id 删除用户信息
     */
    @Delete("DELETE a ,ar from lm_sys_admin a left join lm_sys_adminrole ar on a.id = ar.adminid WHERE a.id = #{id}")
    void deleteAdmin(@Param("id") int id);

    /**
     * 根据参数动态查询用户列表
     * @param condient
     * @return
     */
    @SelectProvider(type = AdminDaoprovider.class, method = "findListByCondient")
    List<Admin> findListByCondient(Map<String, String> condient);

    /**
     * 修改用户信息
     * @param id
     * @param name
     * @param username
     * @param password
     * @param status
     */
    @SelectProvider(type = AdminDaoprovider.class, method = "updateAdmin")
    void updateAdmin(int id,String name,String username, String password, String status);

    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    @Select("SELECT * FROM lm_sys_admin WHERE id = #{id}")
    Admin findAdminById(@Param("id")int id);

    class AdminDaoprovider{

        public String findListByCondient(Map<String, String> condient) {
            String sql = "SELECT * FROM lm_sys_admin";
            if(condient.get("name")!=null){
                sql += " where name = '"+condient.get("name")+"'";
            }
            return sql;
        }

        public String updateAdmin(int id, String name, String username, String password, String status){
            String sql = "UPDATE  lm_sys_admin SET";
            if(!StringUtils.isEmpty(name)){
                sql += " name = '"+name+"',";
            }
            if(!StringUtils.isEmpty(username)){
                sql += " username = '"+username+"',";
            }
            if(!StringUtils.isEmpty(password)){
                sql += " password = '"+password+"',";
            }
            if(!StringUtils.isEmpty(status)){
                sql += " status = '"+status+"',";
            }
            sql = sql.substring(0,sql.length()-1);
            sql+="WHERE id = "+id;
            return sql;
        }
    }
}
