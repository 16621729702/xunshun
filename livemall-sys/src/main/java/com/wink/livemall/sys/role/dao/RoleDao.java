package com.wink.livemall.sys.role.dao;

import com.wink.livemall.sys.admin.dao.AdminDao;
import com.wink.livemall.sys.role.dto.Role;
import org.apache.ibatis.annotations.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleDao {
    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    @Select("SELECT * FROM lm_sys_role WHERE id = #{id}")
    Role findById(@Param("id")Integer id);

    /**
     * 查询所有角色
     * @return
     */
    @Select("SELECT * FROM lm_sys_role order by orderno desc")
    List<Role> findAll();

    /**
     * 删除角色信息
     */
    @Delete("DELETE r , rp from lm_sys_role r left join lm_sys_permissionrole rp on r.id = rp.roleid WHERE r.id = #{id}")
    void deleteRole(@Param("id")Integer id);

    /**
     * 新增角色
     * @param role
     */
    @Insert("insert into lm_sys_role (name,code,orderno,status) values(#{name},#{code},#{orderno},#{status})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    void addRole(Role role);

    /**
     *更新角色信息
     * @param id
     * @param name
     * @param code
     * @param status
     * @param orderno
     */
    @SelectProvider(type = RoleDaoprovider.class, method = "updateRole")
    void updateRole(String id, String name, String code, String status, String orderno);

    class RoleDaoprovider{

        public String updateRole(String id, String name, String code, String status, String orderno){
            String sql = "UPDATE  lm_sys_role SET";
            if(!StringUtils.isEmpty(name)){
                sql += " name = '"+name+"',";
            }
            if(!StringUtils.isEmpty(code)){
                sql += " code = '"+code+"',";
            }
            if(!StringUtils.isEmpty(orderno)){
                sql += " orderno = "+Integer.parseInt(orderno)+",";
            }
            if(!StringUtils.isEmpty(status)){
                sql += " status = '"+status+"',";
            }
            sql = sql.substring(0,sql.length()-1);
            sql+=" WHERE id = "+Integer.parseInt(id);
            return sql;
        }
    }

}
