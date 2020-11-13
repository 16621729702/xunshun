package com.wink.livemall.admin.controller.order;

import com.wink.livemall.admin.controller.marketing.MarketController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LmGoodComment;
import com.wink.livemall.goods.service.LmGoodCommentService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.merch.dto.LmMerchInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("comments")
public class CommentsController {

    private Logger LOG= LoggerFactory.getLogger(CommentsController.class);

    @Autowired
    private LmGoodCommentService lmGoodCommentService;

    @RequestMapping("commentquery")
    public ModelAndView commentquery(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        String goodname = StringUtils.isEmpty(request.getParameter("goodname"))?"":request.getParameter("goodname");
        String content = StringUtils.isEmpty(request.getParameter("content"))?"":request.getParameter("content");
        String isreplay = StringUtils.isEmpty(request.getParameter("isreplay"))?"":request.getParameter("isreplay");
        String goodquality = StringUtils.isEmpty(request.getParameter("goodquality"))?"":request.getParameter("goodquality");
        String serviceattitude = StringUtils.isEmpty(request.getParameter("serviceattitude"))?"":request.getParameter("serviceattitude");
        Map<String,String> condient = new HashMap<String, String>(16);
        condient.put("goodname",goodname);
        condient.put("content",content);
        condient.put("isreplay",isreplay);
        condient.put("goodquality",goodquality);
        condient.put("serviceattitude",serviceattitude);
        model.addAttribute("goodname",goodname);
        model.addAttribute("serviceattitude",serviceattitude);
        model.addAttribute("goodquality",goodquality);
        model.addAttribute("isreplay",isreplay);
        model.addAttribute("content",content);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> lmGoodCommentList =lmGoodCommentService.findByCondient(condient);
        for(Map map:lmGoodCommentList){
            map.put("goodquality",LmGoodComment.goodqualitparsetochinese(map.get("goodquality")+""));
            map.put("serviceattitude",LmGoodComment.serviceparsetochinese(map.get("serviceattitude")+""));
        }
        model.addAttribute("returninfo", PageUtil.startPage(lmGoodCommentList,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",lmGoodCommentList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("totalsize",lmGoodCommentList.size());
        return new ModelAndView("order/commentlist");
    }

    @RequestMapping("replay")
    public ModelAndView replay(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        model.addAttribute("id",id);
        return new ModelAndView("order/commenteditpage");
    }

    @RequestMapping("replaysave")
    @ResponseBody
    public JsonResult replaysave(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String adminreplay = StringUtils.isEmpty(request.getParameter("adminreplay"))?"":request.getParameter("adminreplay");
        try {
            LmGoodComment lmGoodComment = lmGoodCommentService.findById(id);
            lmGoodComment.setAdminreplay(adminreplay);
            lmGoodCommentService.updateService(lmGoodComment);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    @RequestMapping("batchoption")
    @ResponseBody
    @Transactional
    public JsonResult batchoption(HttpServletRequest request){
        String ids = StringUtils.isEmpty(request.getParameter("ids"))?null:request.getParameter("ids");
        String operate = StringUtils.isEmpty(request.getParameter("operate"))?null:request.getParameter("operate");
        try {
            //批量删除
            if("delete".equals(operate)){
                String [] goodids = ids.split(",");
                for(String id:goodids){
                    lmGoodCommentService.delete(id);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }

}
