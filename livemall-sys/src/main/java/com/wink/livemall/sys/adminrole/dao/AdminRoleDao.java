package com.wink.livemall.sys.adminrole.dao;

import com.wink.livemall.sys.adminrole.dto.AdminRole;
import com.wink.livemall.sys.role.dto.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminRoleDao extends  tk.mybatis.mapper.common.Mapper<AdminRole>{
    /**
     * 根据后台用户id 获取角色ids
     * @param id
     * @return
     */
    @Select("SELECT roleid FROM lm_sys_adminrole WHERE adminid = #{id}")
    List<Integer> findRoleListByAdminId(@Param("id") int id);

    /**
     * 根据用户id删除对应关系
     * @param adminid
     */
    @Delete("DELETE from lm_sys_adminrole WHERE adminid = #{adminid}")
    void deleteByAdminId(@Param("adminid")int adminid);
}
