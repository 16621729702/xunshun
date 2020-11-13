package com.wink.livemall.admin.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.service.LmOrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 首页
 */
@Controller
public class IndexController {
    private Logger LOG= LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private LmOrderService lmOrderService;

    /**
     * 主体内容
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(Model model)
    {
        Map<String,String> condient = new HashMap<String, String>(16);
        //查看订单总数
        List<Map<String,Object>> orderlist =lmOrderService.findByCondient(condient);
        //待收货
        condient.put("status","2");
        List<Map<String,Object>> waitgetlist =lmOrderService.findByCondient(condient);
        //查询代发货订单
        condient.put("status","1");
        List<Map<String,Object>> waitsendlist =lmOrderService.findByCondient(condient);
        condient.put("status","0");
        List<Map<String,Object>> waitpaylist =lmOrderService.findByCondient(condient);
        condient.put("status","4");
        List<Map<String,Object>> successlist =lmOrderService.findByCondient(condient);
        condient.put("backstatus","1");
        condient.put("status","");
        List<Map<String,Object>> refundlist =lmOrderService.findByCondient(condient);
        condient.put("backstatus","2");
        List<Map<String,Object>> refundedlist =lmOrderService.findByCondient(condient);
        condient.put("status","5");
        condient.put("backstatus","");
        List<Map<String,Object>> closedlist =lmOrderService.findByCondient(condient);
        model.addAttribute("allnum",orderlist.size());
        model.addAttribute("waitpaynum",waitpaylist.size());
        model.addAttribute("waitsendnum",waitsendlist.size());
        model.addAttribute("waitgetnum",waitgetlist.size());
        model.addAttribute("successnum",successlist.size());
        model.addAttribute("refundnum",refundlist.size());
        model.addAttribute("refundednum",refundedlist.size());
        model.addAttribute("closednum",closedlist.size());
        model.addAttribute("num",1);
        return new ModelAndView("index");
    }

    /**
     * 左侧栏
     * @param model
     * @return
     */
    @RequestMapping("/left")
    public ModelAndView left(Model model)
    {

        return new ModelAndView("left");
    }

    /**
     * 底部
     * @param model
     * @return
     */
    @RequestMapping("/footer")
    public ModelAndView footer(Model model)
    {
        return new ModelAndView("footer");
    }

}  