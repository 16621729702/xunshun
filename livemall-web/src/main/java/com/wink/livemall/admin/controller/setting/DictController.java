package com.wink.livemall.admin.controller.setting;

import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.sys.dict.dto.LmSysDict;
import com.wink.livemall.sys.dict.dto.LmSysDictItem;
import com.wink.livemall.sys.dict.service.DictService;
import com.wink.livemall.sys.dict.service.Dict_itemService;
import com.wink.livemall.sys.setting.dto.Express;
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
import java.util.List;

@Controller
@RequestMapping("dict")
public class DictController {
    private Logger LOG= LoggerFactory.getLogger(DictController.class);

    @Autowired
    private DictService dictService;

    @Autowired
    private Dict_itemService dict_itemService;


    @RequestMapping("query")
    public ModelAndView query(Model model){
        List<LmSysDict> dictList = dictService.findAll();
        model.addAttribute("dictList",dictList);
        model.addAttribute("totalsize",dictList.size());
        return new ModelAndView("setting/dictlist");
    }

    @RequestMapping("addpage")
    public ModelAndView addpage(Model model){
        return new ModelAndView("setting/dictaddpage");
    }

    @RequestMapping("editpage")
    public ModelAndView editpage(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        LmSysDict lmSysDict = dictService.findById(id);
        model.addAttribute("lmSysDict",lmSysDict);
        return new ModelAndView("setting/dicteditpage");
    }


    @RequestMapping("add")
    @ResponseBody
    public JsonResult add(HttpServletRequest request,Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String code = StringUtils.isEmpty(request.getParameter("code"))?null:request.getParameter("code");
        String remark = StringUtils.isEmpty(request.getParameter("remark"))?null:request.getParameter("remark");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"1":request.getParameter("state");
        try {
            LmSysDict lmSysDict = new LmSysDict();
            lmSysDict.setCode(code);
            lmSysDict.setRemark(remark);
            lmSysDict.setName(name);
            lmSysDict.setState(Integer.parseInt(state));
            dictService.addService(lmSysDict);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }

        return new JsonResult();
    }

    @RequestMapping("edit")
    @ResponseBody
    public JsonResult edit(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String code = StringUtils.isEmpty(request.getParameter("code"))?null:request.getParameter("code");
        String remark = StringUtils.isEmpty(request.getParameter("remark"))?null:request.getParameter("remark");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"1":request.getParameter("state");
        try {
            LmSysDict lmSysDict = dictService.findById(id);
            lmSysDict.setCode(code);
            lmSysDict.setRemark(remark);
            lmSysDict.setName(name);
            lmSysDict.setState(Integer.parseInt(state));
            dictService.updateservice(lmSysDict);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    @RequestMapping("delete")
    @ResponseBody
    public JsonResult delete(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        try {
            dictService.deleteService(id);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }
    @RequestMapping("itemdelete")
    @ResponseBody
    public JsonResult itemdelete(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        try {
            dict_itemService.deleteService(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }

    @RequestMapping("itemquery")
    public ModelAndView query(HttpServletRequest request,Model model){
        String code = StringUtils.isEmpty(request.getParameter("code"))?null:request.getParameter("code");
        List<LmSysDictItem> dict_itemList = dict_itemService.findByDictCode(code);
        model.addAttribute("dict_itemList",dict_itemList);
        model.addAttribute("dictcode",code);
        model.addAttribute("totalsize",dict_itemList.size());
        return new ModelAndView("setting/dictitemlist");
    }

    @RequestMapping("itemaddpage")
    public ModelAndView itemaddpage(HttpServletRequest request,Model model){
        String code = StringUtils.isEmpty(request.getParameter("code"))?null:request.getParameter("code");
        model.addAttribute("dictcode",code);
        return new ModelAndView("setting/itemaddpage");
    }

    @RequestMapping("itemeditpage")
    public ModelAndView itemeditpage(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        LmSysDictItem lmSysDictItem = dict_itemService.findById(id);
        model.addAttribute("lmSysDictItem",lmSysDictItem);
        return new ModelAndView("setting/itemeditpage");
    }


    @RequestMapping("itemadd")
    @ResponseBody
    public JsonResult itemadd(HttpServletRequest request,Model model){
        String dictcode = StringUtils.isEmpty(request.getParameter("dictcode"))?null:request.getParameter("dictcode");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String code = StringUtils.isEmpty(request.getParameter("code"))?null:request.getParameter("code");
        String remark = StringUtils.isEmpty(request.getParameter("remark"))?null:request.getParameter("remark");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"1":request.getParameter("state");
        String value = StringUtils.isEmpty(request.getParameter("value"))?"":request.getParameter("value");

        try {
            LmSysDictItem lmSysDictItem = new LmSysDictItem();
            lmSysDictItem.setCode(code);
            lmSysDictItem.setRemark(remark);
            lmSysDictItem.setName(name);
            lmSysDictItem.setValue(value);
            lmSysDictItem.setDict_code(dictcode);
            lmSysDictItem.setState(Integer.parseInt(state));
            dict_itemService.addService(lmSysDictItem);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }

        return new JsonResult();
    }

    @RequestMapping("itemedit")
    @ResponseBody
    public JsonResult itemedit(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String dictcode = StringUtils.isEmpty(request.getParameter("dictcode"))?null:request.getParameter("dictcode");
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String code = StringUtils.isEmpty(request.getParameter("code"))?null:request.getParameter("code");
        String remark = StringUtils.isEmpty(request.getParameter("remark"))?null:request.getParameter("remark");
        String state = StringUtils.isEmpty(request.getParameter("state"))?"1":request.getParameter("state");
        String value = StringUtils.isEmpty(request.getParameter("value"))?"":request.getParameter("value");

        try {
            LmSysDictItem lmSysDictItem =dict_itemService.findById(id);
            lmSysDictItem.setCode(code);
            lmSysDictItem.setDict_code(dictcode);
            lmSysDictItem.setRemark(remark);
            lmSysDictItem.setName(name);
            lmSysDictItem.setValue(value);
            lmSysDictItem.setDict_code(dictcode);
            lmSysDictItem.setState(Integer.parseInt(state));
            dict_itemService.updateService(lmSysDictItem);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


}
