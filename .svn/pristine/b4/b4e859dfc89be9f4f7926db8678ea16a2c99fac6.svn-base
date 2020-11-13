package com.wink.livemall.admin.controller.sysadmin;

import com.wink.livemall.admin.controller.IndexController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.sys.permission.dto.Permission;
import com.wink.livemall.sys.permission.service.PermissionService;
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
 * 后台权限
 */
@Controller
@RequestMapping("permission")
public class PermissionController {
    private Logger LOG= LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    /**
     * 权限项列表
     * @param model
     * @return
     */
    @RequestMapping("query")
    public ModelAndView permissionlist(HttpServletRequest request,Model model){
        List<Permission> list = permissionService.findAll();
        List<Map<String,String>> returnlist = new ArrayList<>();
        for(Permission p:list){
            if(p.getPid()==0){
                Map map = new HashMap();
                map.put("id",p.getId());
                map.put("pid","顶级菜单");
                map.put("name",p.getName());
                map.put("code",p.getCode());
                map.put("orderno",p.getOrderno());
                map.put("status",p.getStatus());
                returnlist.add(map);
                for(Permission cp:list){
                    if(cp.getPid()==p.getId()){
                        Map childmap = new HashMap();
                        childmap.put("id",cp.getId());
                        childmap.put("pid",p.getName());
                        childmap.put("name",cp.getName());
                        childmap.put("code",cp.getCode());
                        childmap.put("orderno",cp.getOrderno());
                        childmap.put("status",cp.getStatus());
                        returnlist.add(childmap);
                    }
                }
            }
        }
        model.addAttribute("permissionlist",returnlist);
        return new ModelAndView("admin/permissionlist1");
    }

    /**
     * 添加权限项页面
     * @param model
     * @return
     */
    @RequestMapping("insertpage")
    public ModelAndView insertpage(HttpServletRequest request,Model model){
        model.addAttribute("permissionlist",permissionService.findAll());
        return new ModelAndView("admin/insertpermission");
    }

    /**
     * 修改权限项页面
     * @param model
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        if(id!=null){
            Permission permission = permissionService.findPermissionById(Integer.parseInt(id));
            model.addAttribute("permission",permission);
            model.addAttribute("permissionlist",permissionService.findAll());
        }
        return new ModelAndView("admin/editpermission");
    }


    /**
     *  新增权限项
     * @return
     */
    @RequestMapping("insert")
    @ResponseBody
    public JsonResult insertPermission(HttpServletRequest request, Model model){
        String pid = StringUtils.isEmpty(request.getParameter("pid"))?null:request.getParameter("pid");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String code = StringUtils.isEmpty(request.getParameter("code"))?null:request.getParameter("code");
        String order = StringUtils.isEmpty(request.getParameter("order"))?null:request.getParameter("order");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");
        try {
            permissionService.insertPermission(pid,name,code,order,status);
        }catch (Exception e){
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     *  修改权限项
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    public JsonResult editPermission(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String pid = StringUtils.isEmpty(request.getParameter("pid"))?null:request.getParameter("pid");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String code = StringUtils.isEmpty(request.getParameter("code"))?null:request.getParameter("code");
        String order = StringUtils.isEmpty(request.getParameter("order"))?null:request.getParameter("order");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");
        try {
            if(id!=null){
                permissionService.editPermission(id,pid,name,code,order,status);
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
     *  删除权限项
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public JsonResult deleteAdmin(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            if(id!=null){
                permissionService.deletepermission(Integer.parseInt(id));
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
