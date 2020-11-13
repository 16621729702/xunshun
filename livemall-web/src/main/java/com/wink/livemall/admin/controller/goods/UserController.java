package com.wink.livemall.admin.controller.goods;

import java.util.List;

import com.wink.livemall.admin.controller.ExcelController.ExportController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wink.livemall.goods.dto.User;
import com.wink.livemall.goods.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController 
{
    private Logger LOG= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/query")
    public User testQuery() {
        return userService.selectUserByName("Daisy");
    }

    @RequestMapping("/insert")
    public List<User> testInsert() {
        userService.insertService();
        return userService.selectAllUser();
    }

    
    @RequestMapping("/changemoney")
    public List<User> testchangemoney() {
        userService.changemoney();
        return userService.selectAllUser();
    }

    @RequestMapping("/delete")
    public String testDelete() {
        userService.deleteService(3);
        return "OK";
    }
    
}