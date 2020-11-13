package com.wink.livemall.admin.controller.sysadmin;

import com.wink.livemall.admin.controller.IndexController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.Md5Util;
import com.wink.livemall.sys.admin.dto.Admin;
import com.wink.livemall.sys.admin.service.AdminService;
import com.wink.livemall.sys.adminrole.dto.AdminRole;
import com.wink.livemall.sys.adminrole.service.AdminRoleService;
import com.wink.livemall.sys.role.dto.Role;
import com.wink.livemall.sys.role.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台用户
 */
@Controller
@RequestMapping("admin")
public class AdminController {
    private Logger LOG= LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminRoleService adminRoleService;
    /**
     *  动态查询用户列表
     * @return
     */
    @RequestMapping("query")
    public ModelAndView index(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        Map<String,String> condient = new HashMap<>(16);
        List<Admin> admins = adminService.findListByCondient(condient);
        model.addAttribute("adminlist",admins);
        return new ModelAndView("admin/adminlist");
    }

    /**
     *  添加用户页面
     * @return
     */
    @RequestMapping("insertpage")
    public ModelAndView insertpage(HttpServletRequest request, Model model){
        //添加角色列表
        model.addAttribute("rolelist",roleService.findAll());
        return new ModelAndView("admin/insertadmin");
    }



    /**
     *  修改用户界面
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        if(id!=null){
            Admin admin = adminService.findAdminById(Integer.parseInt(id));
            admin.setPassword(Md5Util.md5jiami(admin.getPassword()));
            List<Integer> adminRoles = adminRoleService.findRoleListByAdminId(admin.getId());
            List<Role> roles = roleService.findAll();
            List<Map<String,String>> returnlist = new ArrayList<>();
            //设置选中项
            for(Role role:roles){
                Map<String,String> map = new HashMap();
                map.put("id",role.getId()+"");
                map.put("name",role.getName());
                for(Integer roleid:adminRoles){
                    if(roleid.equals(role.getId())){
                        map.put("ischecked","selected");
                        break;
                    }else{
                        map.put("ischecked","");
                    }
                }
                returnlist.add(map);
            }
            model.addAttribute("admin",admin);
            model.addAttribute("rolelist",returnlist);
        }
        return new ModelAndView("admin/editadmin");
    }

    /**
     *  新增用户信息
     * @return
     */
    @RequestMapping("insert")
    @ResponseBody
    public JsonResult insert(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String username = StringUtils.isEmpty(request.getParameter("username"))?null:request.getParameter("username");
        String password = StringUtils.isEmpty(request.getParameter("password"))?null:request.getParameter("password");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");
        String roles = StringUtils.isEmpty(request.getParameter("roles"))?null:request.getParameter("roles");
        try {
            adminService.insertAdmin(name,username,Md5Util.MD5(password),status,roles);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     *  修改用户信息
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    public JsonResult edit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String username = StringUtils.isEmpty(request.getParameter("username"))?null:request.getParameter("username");
        String password = StringUtils.isEmpty(request.getParameter("password"))?null:request.getParameter("password");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");
        String roles = StringUtils.isEmpty(request.getParameter("roles"))?null:request.getParameter("roles");
        try {
            if(id!=null){
                adminService.editAdmin(id,name,username,password!=null?Md5Util.MD5(password):null,status,roles);
            }else{
                return new JsonResult(JsonResult.ERROR,"参数异常");
            }
        }catch (Exception e){
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     *  删除用户
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public JsonResult delete(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            if(id!=null){
                adminService.deleteAdmin(id);
            }else{
                return new JsonResult(JsonResult.ERROR,"参数异常");
            }
        }catch (Exception e){
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }

}
