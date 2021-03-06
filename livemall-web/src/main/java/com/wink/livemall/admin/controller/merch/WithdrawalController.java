package com.wink.livemall.admin.controller.merch;

import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.dto.LmSellCate;
import com.wink.livemall.merch.dto.LmWithdrawal;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.merch.service.LmWithdrawalService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 提现申请管理
 */
@Controller
@RequestMapping("withdrawal")
public class WithdrawalController {
    private Logger LOG= LoggerFactory.getLogger(WithdrawalController.class);

    @Autowired
    private LmWithdrawalService lmWithdrawalService;

    @Autowired
    private LmMerchInfoService lmMerchInfoService;

    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request, Model model){
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        model.addAttribute("page",page);
        model.addAttribute("pagesize",pagesize);
        List<Map<String,Object>> returnlist =  lmWithdrawalService.findList();
        model.addAttribute("totalsize",returnlist.size());
        model.addAttribute("returninfo", PageUtil.startPage(returnlist,Integer.parseInt(page),Integer.parseInt(pagesize)));
        model.addAttribute("totalpage",returnlist.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("/merch/withdrawallist");
    }
    /**
     * 修改分类页面
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView categoryeditpage(HttpServletRequest request, Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?"0":request.getParameter("id");
        id = id.replaceAll(",","");
        Map<String,Object> mapinfo = lmWithdrawalService.findinfoById(Integer.parseInt(id));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("merch/withdrawaleditpage");
        model.addAttribute("mapinfo",mapinfo);

        return modelAndView;
    }


    @RequestMapping("editthis")
    @ResponseBody
    public JsonResult editgoodcategory(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        id = id.replaceAll(",","");
        String status = StringUtils.isEmpty(request.getParameter("status"))?null:request.getParameter("status");
        try {
            if(id==null||status==null){
                return  new JsonResult(JsonResult.ERROR,"参数异常");
            }
            LmWithdrawal lmWithdrawal = lmWithdrawalService.findById(Integer.parseInt(id));
            lmWithdrawal.setStatus(Integer.parseInt(status));
            if(!"1".equals(status)){
                LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(lmWithdrawal.getMerchid()+"");
                lmMerchInfo.setCredit((null==lmMerchInfo.getCredit()?BigDecimal.ZERO:lmMerchInfo.getCredit()) .add(lmWithdrawal.getMoney()));//(null==lmMerchInfo.getCredit()?BigDecimal.ZERO:lmMerchInfo.getCredit()).add(order.getRealpayprice())
                lmMerchInfoService.updateService(lmMerchInfo);
            }
            lmWithdrawalService.updateService(lmWithdrawal);
        } catch (Exception e) {
            e.printStackTrace();
             LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }
}
