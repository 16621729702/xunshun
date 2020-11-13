package com.wink.livemall.admin.controller.help;

import com.wink.livemall.admin.controller.goods.GoodController;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.help.dao.LmFeedbackDao;
import com.wink.livemall.help.dao.LmHelpInfoDao;
import com.wink.livemall.help.dto.LmFeedback;
import com.wink.livemall.help.dto.LmHelpCategory;
import com.wink.livemall.help.dto.LmHelpInfo;
import com.wink.livemall.help.service.LmFeedbackService;
import com.wink.livemall.help.service.LmHelpCategoryService;
import com.wink.livemall.help.service.LmHelpInfoService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.service.LmMemberService;
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
import java.util.*;

@Controller
@RequestMapping("help")
public class HelpController {
    private Logger LOG= LoggerFactory.getLogger(HelpController.class);


    @Autowired
    private LmFeedbackService lmFeedbackService;
    @Autowired
    private LmHelpInfoService lmHelpInfoService;
    @Autowired
    private LmHelpCategoryService lmHelpCategoryService;
    @Autowired
    private LmMemberService lmMemberService;

    /**
     * 知识库内容列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("infoquery")
    public ModelAndView infoquery(HttpServletRequest request, Model model){
        List<LmHelpInfo> lmHelpInfoList = lmHelpInfoService.findAll();
        List<Map<String,String>> returninfo = new ArrayList<>();
        for(LmHelpInfo lmHelpInfo:lmHelpInfoList){
            Map<String,String> map = new HashMap<>();
            map.put("title",lmHelpInfo.getTitle());
            map.put("id",lmHelpInfo.getId()+"");
            map.put("content",lmHelpInfo.getContent());
            map.put("state",lmHelpInfo.getState()+"");
            map.put("sort",lmHelpInfo.getSort()+"");
            LmHelpCategory lmHelpCategory = lmHelpCategoryService.findById(lmHelpInfo.gethCategoryId()+"");
            map.put("categoryname",lmHelpCategory.getName());
            returninfo.add(map);
        }
        model.addAttribute("returninfo",returninfo);
        model.addAttribute("totalsize",lmHelpInfoList.size());
        return new ModelAndView("/help/infolist");
    }

    /**
     * 知识库内容添加
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("infoaddpage")
    public ModelAndView infoaddpage(HttpServletRequest request, Model model){
        List<LmHelpCategory> lmHelpCategoryList = lmHelpCategoryService.findAll();
        model.addAttribute("lmHelpCategoryList",lmHelpCategoryList);
        return new ModelAndView("/help/infoaddpage");
    }
    /**
     * 知识库内容修改
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("infoeditpage")
    public ModelAndView infoeditpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        LmHelpInfo lmHelpInfo = lmHelpInfoService.findById(id);
        List<LmHelpCategory> lmHelpCategoryList = lmHelpCategoryService.findAll();
        model.addAttribute("lmHelpInfo",lmHelpInfo);
        model.addAttribute("lmHelpCategoryList",lmHelpCategoryList);
        return new ModelAndView("/help/infoeditpage");
    }

    /**
     * 添加信息
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("infoadd")
    @ResponseBody
    public JsonResult infoadd(HttpServletRequest request, Model model){
        String h_category_id = StringUtils.isEmpty(request.getParameter("h_category_id"))?"0":request.getParameter("h_category_id");
        String title = StringUtils.isEmpty(request.getParameter("title"))?"":request.getParameter("title");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"0":request.getParameter("state");
        String sort = StringUtils.isEmpty(request.getParameter("sort"))?"0":request.getParameter("sort");
        String content = StringUtils.isEmpty(request.getParameter("content"))?"":request.getParameter("content");
        try {
            LmHelpInfo lmHelpInfo  = new LmHelpInfo();
            lmHelpInfo.setContent(content);
            lmHelpInfo.setCreateAt(new Date());
            lmHelpInfo.sethCategoryId(Integer.parseInt(h_category_id));
            lmHelpInfo.setSort(Integer.parseInt(sort));
            lmHelpInfo.setState(Integer.parseInt(state));
            lmHelpInfo.setTitle(title);
            lmHelpInfo.setUpdateAt(new Date());
            lmHelpInfoService.insertService(lmHelpInfo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * 编辑信息保存
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("infoedit")
    @ResponseBody
    public JsonResult infoedit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String h_category_id = StringUtils.isEmpty(request.getParameter("h_category_id"))?"0":request.getParameter("h_category_id");
        String title = StringUtils.isEmpty(request.getParameter("title"))?"":request.getParameter("title");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"0":request.getParameter("state");
        String sort = StringUtils.isEmpty(request.getParameter("sort"))?"0":request.getParameter("sort");
        String content = StringUtils.isEmpty(request.getParameter("content"))?"":request.getParameter("content");
        try {
            LmHelpInfo lmHelpInfo  = lmHelpInfoService.findById(id);
            lmHelpInfo.setContent(content);
            lmHelpInfo.setUpdateAt(new Date());
            lmHelpInfo.sethCategoryId(Integer.parseInt(h_category_id));
            lmHelpInfo.setSort(Integer.parseInt(sort));
            lmHelpInfo.setState(Integer.parseInt(state));
            lmHelpInfo.setTitle(title);
            lmHelpInfoService.updateService(lmHelpInfo);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * 删除信息
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("infodelete")
    @ResponseBody
    public JsonResult infodelete(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            lmHelpInfoService.deleteService(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 帮助分类列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("categoryquery")
    public ModelAndView categoryquery(HttpServletRequest request, Model model){
        List<LmHelpCategory> lmHelpCategoryList = lmHelpCategoryService.findAll();
        model.addAttribute("returninfo",lmHelpCategoryList);
        model.addAttribute("totalsize",lmHelpCategoryList.size());
        return new ModelAndView("/help/categorylist");
    }

    /**
     * 帮助分类添加
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("categoryaddpage")
    public ModelAndView categoryaddpage(HttpServletRequest request, Model model){
        return new ModelAndView("/help/categoryaddpage");
    }

    /**
     * 添加帮助分类
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("categoryadd")
    @ResponseBody
    public JsonResult categoryadd(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String isshow = StringUtils.isEmpty(request.getParameter("isshow"))?"0":request.getParameter("isshow");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"0":request.getParameter("state");
        String sort = StringUtils.isEmpty(request.getParameter("sort"))?"0":request.getParameter("sort");
        try {
            LmHelpCategory lmHelpCategory = new LmHelpCategory();
            lmHelpCategory.setCreateAt(new Date());
            lmHelpCategory.setIsshow(Integer.parseInt(isshow));
            lmHelpCategory.setState(Integer.parseInt(state));
            lmHelpCategory.setSort(Integer.parseInt(sort));
            lmHelpCategory.setUpdateAt(new Date());
            lmHelpCategory.setPid(0);
            lmHelpCategory.setName(name);
            lmHelpCategoryService.insertService(lmHelpCategory);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * 编辑帮助分类
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("categoryedit")
    @ResponseBody
    public JsonResult categoryedit(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String isshow = StringUtils.isEmpty(request.getParameter("isshow"))?"0":request.getParameter("isshow");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"0":request.getParameter("state");
        String sort = StringUtils.isEmpty(request.getParameter("sort"))?"0":request.getParameter("sort");
        try {
            LmHelpCategory lmHelpCategory = lmHelpCategoryService.findById(id);
            lmHelpCategory.setIsshow(Integer.parseInt(isshow));
            lmHelpCategory.setState(Integer.parseInt(state));
            lmHelpCategory.setSort(Integer.parseInt(sort));
            lmHelpCategory.setUpdateAt(new Date());
            lmHelpCategory.setName(name);
            lmHelpCategoryService.updateService(lmHelpCategory);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * 删除帮助分类
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("categorydelete")
    @ResponseBody
    public JsonResult categorydelete(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            lmHelpCategoryService.deleteService(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 帮助分类修改页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("categoryeditpage")
    public ModelAndView categoryeditpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        LmHelpCategory lmHelpCategory = lmHelpCategoryService.findById(id);
        model.addAttribute("lmHelpCategory",lmHelpCategory);
        return new ModelAndView("/help/categoryeditpage");
    }

    /**
     * 反馈信息列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("feedbackquery")
    public ModelAndView feedbackquery(HttpServletRequest request, Model model){
        String type = StringUtils.isEmpty(request.getParameter("type"))?null:request.getParameter("type");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        model.addAttribute("type",type);
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        Map<String,String> condient = new HashMap<>(16);
        condient.put("type",type);
        List<LmFeedback> lmFeedbackList = lmFeedbackService.findByCondient(condient);
        List<Map<String,Object>> returninfo = new ArrayList<>();
        for(LmFeedback lmFeedback:lmFeedbackList){
            Map<String,Object> map = new HashMap<>();
            map.put("id",lmFeedback.getId()+"");
            map.put("content",lmFeedback.getContent());
            map.put("images",lmFeedback.getImages());
            map.put("type",LmFeedback.parsetochinese(lmFeedback.getType()));
            map.put("state",lmFeedback.getState());
            if(lmFeedback.getReply_time()!=null){
                map.put("reply_time", DateUtils.sdf_yMdHms.format(lmFeedback.getReply_time()));
            }
            map.put("reply_content",lmFeedback.getReply_content());
            LmMember lmMember = lmMemberService.findById(lmFeedback.getMember_id()+"");
            if(lmMember!=null){
                map.put("membername",lmMember.getNickname());
            }
            returninfo.add(map);
        }
        model.addAttribute("totalpage",lmFeedbackList.size()/Integer.parseInt(pagesize)+1);
        model.addAttribute("returninfo", PageUtil.startPage(returninfo,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalsize",lmFeedbackList.size());
        return new ModelAndView("/help/feedbacklist");
    }

    @RequestMapping("feedbackeditpage")
    public ModelAndView feedbackeditpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");

        LmFeedback lmFeedback = lmFeedbackService.findById(id);
        model.addAttribute("lmFeedback",lmFeedback);
        return new ModelAndView("/help/feedbackedit");
    }
    /**
     * 回复反馈信息
     * @param request
     * @return
     */
    @RequestMapping("feedbackedit")
    @ResponseBody
    public JsonResult feedbackedit(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String content = StringUtils.isEmpty(request.getParameter("content"))?null:request.getParameter("content");
        try {
           LmFeedback lmFeedback = lmFeedbackService.findById(id);
           if(lmFeedback!=null){
               lmFeedback.setReply_content(content);
               lmFeedback.setReply_time(new Date());
               lmFeedbackService.updateService(lmFeedback);
           }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    /**
     * 删除反馈信息
     * @param request
     * @return
     */
    @RequestMapping("feedbackdelete")
    @ResponseBody
    public JsonResult feedbackdelete(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            lmFeedbackService.deleteService(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }
}
