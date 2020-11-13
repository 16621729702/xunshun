package com.wink.livemall.sys.permissionrole.dao;

import org.apache.ibatis.annotations.*;

@Mapper
public interface PermissionRoleDao {
    /**
     * 新增角色权限关系
     * @param roleid
     * @param permissionid
     */
    @Insert("insert into lm_sys_permissionrole (roleid,permissionid) values(#{roleid},#{permissionid})")
    void addPermissionrole(@Param("roleid")int roleid, @Param("permissionid")int permissionid);

    /**
     * 根据角色删除对关系
     * @param roleid
     */
    @Delete("DELETE from lm_sys_permissionrole WHERE roleid = #{roleid}")
    void deleteRolePermission(@Param("roleid")int roleid);
}
