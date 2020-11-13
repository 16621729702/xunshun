package com.wink.livemall.admin.controller;

import com.wink.livemall.admin.controller.live.LiveController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.Md5Util;
import com.wink.livemall.sys.admin.dto.Admin;
import com.wink.livemall.sys.admin.service.AdminService;
import com.wink.livemall.utils.cache.redis.RedisUtil;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LoginController {
    private Logger LOG= LoggerFactory.getLogger(LoginController.class);
    @Resource
    private AdminService adminService;
    
   // @Autowired
    //private RedisUtil redisUtil;
    
    @RequestMapping("/login")
    public ModelAndView login()
    {
    	
  //  	redisUtil.set("11","aaaa");
    	
  // 	System.err.println(redisUtil.get("11"));
    	
        return new ModelAndView("login");
    }

    @RequestMapping("/logincheck")
    @ResponseBody
    public JsonResult logincheck(HttpServletRequest request)
    {
        String username = StringUtils.isEmpty(request.getParameter("username"))?null:request.getParameter("username");
        String password = StringUtils.isEmpty(request.getParameter("password"))?null:request.getParameter("password");
        if(username!=null&&password!=null){
            Admin admin = adminService.findAdminbyUsername(username);
            if(admin!=null){
                if(admin.getPassword().equals(Md5Util.MD5(password))){
                    Subject subject = SecurityUtils.getSubject();
                    UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
                    try {
                        subject.login(usernamePasswordToken); // 完成登录
                        Admin user = (Admin) subject.getPrincipal();
                        request.getSession().setAttribute("user", user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new JsonResult();
                }else{
                    return new JsonResult(JsonResult.ERROR,"密码不存在");
                }
            }else {
                return new JsonResult(JsonResult.ERROR,"用户名不存在");
            }
        }
        return new JsonResult(JsonResult.ERROR,"用户名密码不正确");
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request)
    {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new ModelAndView("login");
    }
}
  