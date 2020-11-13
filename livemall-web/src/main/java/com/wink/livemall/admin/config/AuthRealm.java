package com.wink.livemall.admin.config;

import com.wink.livemall.goods.service.UserService;
import com.wink.livemall.sys.admin.dto.Admin;
import com.wink.livemall.sys.admin.service.AdminService;
import com.wink.livemall.sys.adminrole.service.AdminRoleService;
import com.wink.livemall.sys.permission.dto.Permission;
import com.wink.livemall.sys.permission.service.PermissionService;
import com.wink.livemall.sys.role.dto.Role;
import com.wink.livemall.sys.role.service.RoleService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限认证配置
 */
public class AuthRealm extends AuthorizingRealm {

    @Resource
    private AdminService adminService;
    @Resource
    private RoleService roleService;
    @Resource
    private AdminRoleService adminRoleService;
    @Resource
    private PermissionService permissionService;
    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Admin user = (Admin) principalCollection.fromRealm(this.getClass().getName()).iterator().next();// 获取session中的用户
        Set<String> permissions = new HashSet<>();
        Set<String> roles=new HashSet<>();
        if(user!=null){
            List<Integer> roleList = adminRoleService.findRoleListByAdminId(user.getId());
            for(Integer id:roleList){
                Role role = roleService.findById(id);
                roles.add(role.getName());
                List<Permission> permissionsList = permissionService.findListByRoleid(id);
                for(Permission permission:permissionsList){
                    permissions.add(permission.getCode());
                }
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //info.addStringPermissions(permissions);// 将权限放入shiro中.
        info.setStringPermissions(permissions);
        info.setRoles(roles);
        return info;
    }

    /**
     * 登录 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken utoken = (UsernamePasswordToken) authenticationToken;// 获取用户输入的token
        String username = utoken.getUsername();
        Admin user = adminService.findAdminbyUsername(username);
        if(user!=null) {
            return new SimpleAuthenticationInfo(user, user.getPassword(), this.getClass().getName());// 放入shiro.调用CredentialsMatcher检验密码
        }else {
            return null;
        }
    }
}
