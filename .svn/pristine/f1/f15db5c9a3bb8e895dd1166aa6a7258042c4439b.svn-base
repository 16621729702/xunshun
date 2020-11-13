package com.wink.livemall.admin.controller.sysadmin;

import com.wink.livemall.admin.controller.IndexController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.sys.permission.dto.Permission;
import com.wink.livemall.sys.permission.service.PermissionService;
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
 * 后台用户角色
 */
@Controller
@RequestMapping("role")
public class RoleController {
    private Logger LOG= LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    /**
     * 角色列表
     * @return
     */
    @RequestMapping("query")
    public ModelAndView index(Model model){
        model.addAttribute("rolelist",roleService.findAll());
        return new ModelAndView("admin/rolelist");
    }

    /**
     * 修改角色信息页面
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView editview(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        if(!StringUtils.isEmpty(id)){
            //拉去权限列表
            List<Permission> permissionList = permissionService.findListByRoleid(Integer.parseInt(id));
            Role role = roleService.findById(Integer.parseInt(id));
            List<Permission> allpermissionlist = permissionService.findAll();
            List<Map<String,String>> returnlist = new ArrayList<>();
            //排序
            for(Permission p:allpermissionlist){
                if(p.getPid()==0&&"ACTIVE".equals(p.getStatus())){
                    Map<String,String> map = new HashMap();
                    map.put("id",p.getId()+"");
                    map.put("name",p.getName());
                    returnlist.add(map);
                    for(Permission cp:allpermissionlist){
                        if(cp.getPid()==p.getId()&&"ACTIVE".equals(cp.getStatus())){
                            Map<String,String> childmap = new HashMap();
                            childmap.put("id",cp.getId()+"");
                            childmap.put("name",cp.getName());
                            returnlist.add(childmap);
                        }
                    }
                }
            }
            //设置选中状态
            for(Map<String,String> map:returnlist){
                for(Permission p:permissionList){
                    if(map.get("id").equals(p.getId()+"")){
                        map.put("ischecked","selected");
                        break;
                    }else{
                        map.put("ischecked","");
                    }
                }
            }
            model.addAttribute("role",role);
            model.addAttribute("allpermissionlist",returnlist);
        }
        return new ModelAndView("admin/editrole");
    }


    /**
     * 新增角色信息页面
     * @return
     */
    @RequestMapping("insertpage")
    public ModelAndView insertpage(HttpServletRequest request,Model model){
        //拉去权限列表
        List<Permission> permissionList = permissionService.findAll();
        List<Map<String,String>> returnlist = new ArrayList<>();
        for(Permission p:permissionList){
            if(p.getPid()==0&&"ACTIVE".equals(p.getStatus())){
                Map map = new HashMap();
                map.put("id",p.getId());
                map.put("name",p.getName());
                List<Map<String,String>> childlist = new ArrayList<>();
                for(Permission cp:permissionList){
                    if(cp.getPid()==p.getId()&&"ACTIVE".equals(cp.getStatus())){
                        Map childmap = new HashMap();
                        childmap.put("id",cp.getId());
                        childmap.put("name",cp.getName());
                        childlist.add(childmap);
                    }
                }
                map.put("childlist",childlist);
                returnlist.add(map);
            }
        }
        model.addAttribute("permissionlist",returnlist);
        return new ModelAndView("admin/insertrole");
    }


    /**
     * 新增角色信息
     * @return
     */
    @RequestMapping("insert")
    @ResponseBody
    public JsonResult insert(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String code = StringUtils.isEmpty(request.getParameter("code"))?null:request.getParameter("code");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");
        String orderno = StringUtils.isEmpty(request.getParameter("orderno"))?null:request.getParameter("orderno");
        String permissions = StringUtils.isEmpty(request.getParameter("permissions"))?null:request.getParameter("permissions");
        if(name!=null&&code!=null&&status!=null&orderno!=null&&permissions!=null){
            Role role = new Role();
            role.setName(name);
            role.setCode(code);
            role.setOrderno(Integer.parseInt(orderno));
            role.setStatus(status);
            roleService.addRole(role,permissions);
        }else{
            return new JsonResult(JsonResult.ERROR,"参数异常");
        }
        return new JsonResult();
    }


    /**
     * 修改角色信息
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    public JsonResult edit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String code = StringUtils.isEmpty(request.getParameter("code"))?null:request.getParameter("code");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");
        String orderno = StringUtils.isEmpty(request.getParameter("orderno"))?null:request.getParameter("orderno");
        String permissions = StringUtils.isEmpty(request.getParameter("permissions"))?null:request.getParameter("permissions");
        if(id!=null){
            roleService.updateRole(id,name,code,status,orderno,permissions);
        }else{
            return new JsonResult(JsonResult.ERROR,"参数异常");
        }
        return new JsonResult();
    }

    /**
     * 修改角色信息
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public JsonResult delete(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            roleService.deleteRole(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }
}
