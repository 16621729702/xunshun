package com.wink.livemall.sys.permission.dao;

import com.wink.livemall.sys.admin.dao.AdminDao;
import com.wink.livemall.sys.admin.dto.Admin;
import com.wink.livemall.sys.permission.dto.Permission;
import org.apache.ibatis.annotations.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface PermissionDao {

    /**
     * 根据角色查询权限项
     */
    @Select("SELECT p.* FROM lm_sys_permission p,lm_sys_permissionrole pr WHERE p.id = pr.permissionid and pr.roleid = #{roleid}")
    List<Permission> findPermissionsByRoleId(@Param("roleid") int roleid);

    /**
     * 查询所有
     * @return
     */
    @Select("SELECT * FROM lm_sys_permission order by orderno desc")
    List<Permission> findAll();

    /**
     * 根据pid查询列表
     * @param pid
     * @return
     */
    @Select("SELECT * FROM lm_sys_permission WHERE pid = #{pid}")
    List<Permission> findListByPid(@Param("pid") int pid);

    /**
     * 根据id查询实体
     * @param id
     * @return
     */
    @Select("SELECT * FROM lm_sys_permission WHERE id = #{id}")
    Permission findPermissionById(@Param("id")int id);

    /**
     * 删除实体
     * @param id
     */
    @Delete("DELETE from lm_sys_permission WHERE id = #{id}")
    void deletePermission(@Param("id")int id);

    /**
     * 新增实体
     * @param pid
     * @param name
     * @param code
     * @param orderno
     * @param status
     */
    @Insert("INSERT INTO lm_sys_permission(pid, name,code,orderno,status) VALUES(#{pid}, #{name}, #{code}, #{orderno}, #{status})")
    void insertPermission(@Param("pid")int pid, @Param("name")String name,@Param("code")String code, @Param("orderno")int orderno, @Param("status")String status);

    /**
     * 修改权限项
     * @param id
     * @param pid
     * @param name
     * @param code
     * @param orderno
     * @param status
     */
    @SelectProvider(type = PermissionDaoprovider.class, method = "editPermission")
    void editPermission(String id, String pid, String name, String code, String orderno, String status);

    class PermissionDaoprovider{


        public String editPermission(String id, String pid, String name, String code, String orderno, String status){
            String sql = " UPDATE  lm_sys_permission p  SET ";
            if(!StringUtils.isEmpty(pid)){
                sql += " p.pid = "+Integer.parseInt(pid)+",";
            }
            if(!StringUtils.isEmpty(name)){
                sql += " p.name = '"+name+"',";
            }
            if(!StringUtils.isEmpty(code)){
                sql += " p.code = '"+code+"',";
            }
            if(!StringUtils.isEmpty(status)){
                sql += " p.status = '"+status+"',";
            }
            if(!StringUtils.isEmpty(orderno)){
                sql += " p.orderno = "+Integer.parseInt(orderno)+",";
            }
            sql = sql.substring(0,sql.length()-1);
            sql+="  WHERE id = "+Integer.parseInt(id);
            return sql;
        }
    }
}
