package com.wink.livemall.admin.controller.goods;

import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.goods.dto.*;
import com.wink.livemall.goods.service.*;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 商品管理
 */
@Controller
@RequestMapping("good")
public class GoodController {
    @Autowired
    private GoodService goodService;
    @Autowired
    private GoodCategoryService goodCategoryService;
    @Autowired
    private GoodSpecService goodSpecService;
    @Autowired
    private GoodSpecitemService goodSpecitemService;
    @Autowired
    private GoodSpecOptionService goodSpecOptionService;
    @Autowired
    private LmMerchInfoService lmMerchInfoService;
    private Logger LOG= LoggerFactory.getLogger(GoodController.class);

    /**
     * 商品查询页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("query")
    public ModelAndView list(HttpServletRequest request, Model model){
        String name = StringUtils.isEmpty(request.getParameter("name"))?null:request.getParameter("name");
        String categoryid = StringUtils.isEmpty(request.getParameter("categoryid"))?null:request.getParameter("categoryid");
        String merchid = StringUtils.isEmpty(request.getParameter("merchid"))?null:request.getParameter("merchid");

        String state = StringUtils.isEmpty(request.getParameter("state"))?"":request.getParameter("state");
        String startdate = StringUtils.isEmpty(request.getParameter("startdate"))?null:request.getParameter("startdate");
        String enddate = StringUtils.isEmpty(request.getParameter("enddate"))?null:request.getParameter("enddate");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        Map<String,String> condient = new HashMap<>(16);
        condient.put("title",name);
        condient.put("page",page);
        condient.put("pagesize",pagesize);
        condient.put("categoryid",categoryid);
        condient.put("state",state);
        condient.put("merchid",merchid);
        condient.put("startdate",startdate);
        condient.put("enddate",enddate);
        List<Map> list = goodService.findListByCondient(condient);
        List<GoodCategory> categoryList = goodCategoryService.selectAll();
        List<Map<String,String>> returnlist = new ArrayList<>();
        for(GoodCategory g:categoryList){
            if(g.getParent_id()==0){
                Map map = new HashMap();
                map.put("id",g.getId());
                map.put("name",g.getName());
                if(categoryid!=null&&categoryid.equals(g.getId()+"")){
                    map.put("isselect","selected");
                }else{
                    map.put("isselect","");
                }
                returnlist.add(map);
                for(GoodCategory cp:categoryList){
                    if(cp.getParent_id()==g.getId()){
                        Map childmap = new HashMap();
                        childmap.put("id",cp.getId());
                        childmap.put("name",cp.getName());
                        if(categoryid!=null&&categoryid.equals(cp.getId()+"")){
                            childmap.put("isselect","selected");
                        }else{
                            childmap.put("isselect","");
                        }
                        returnlist.add(childmap);
                    }
                }
            }
        }
        //参数
        model.addAttribute("condient",condient);
        //类别
        model.addAttribute("categoryList",returnlist);
        //总数
        model.addAttribute("totalsize",list.size());
        //商品信息
        model.addAttribute("returninfo", PageUtil.startPage(list,Integer.parseInt(page),Integer.parseInt(pagesize)));
        //总页数
        model.addAttribute("totalpage",list.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("/good/goodlist");
    }

    /**
     * 商品查询页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("merchquery")
    public ModelAndView merchquery(HttpServletRequest request, Model model){
        String merchid = StringUtils.isEmpty(request.getParameter("merchid"))?null:request.getParameter("merchid");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        Map<String,String> condient = new HashMap<>(16);
        condient.put("page",page);
        condient.put("pagesize",pagesize);
        condient.put("merchid",merchid);
        List<Map> list = goodService.findListByCondient(condient);
        //参数
        model.addAttribute("condient",condient);
        //总数
        model.addAttribute("totalsize",list.size());
        //商品信息
        model.addAttribute("returninfo", PageUtil.startPage(list,Integer.parseInt(page),Integer.parseInt(pagesize)));
        //总页数
        model.addAttribute("totalpage",list.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("/merch/merchgoodlist");
    }


    /**
     * 商品编辑页面
     * @param request
     * @return
     */
    @RequestMapping("editpage")
    public ModelAndView edit(HttpServletRequest request,Model model){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        //商品信息
        Good good = null;
        if(id!=null){
             good = goodService.findById(Integer.parseInt(id));
            model.addAttribute("good",good);
        }
        //商品分类下拉
        List<GoodCategory> categoryList = goodCategoryService.selectAll();
        List<Map<String,String>> returnlist = new ArrayList<>();
        for(GoodCategory g:categoryList){
            if(g.getParent_id()==0){
                Map map = new HashMap();
                map.put("id",g.getId());
                map.put("name",g.getName());
                map.put("isuse","disabled");
                map.put("isselected","");
                returnlist.add(map);
                for(GoodCategory cp:categoryList){
                    if(cp.getParent_id()==g.getId()){
                        Map childmap = new HashMap();
                        childmap.put("id",cp.getId());
                        childmap.put("name",cp.getName());
                        childmap.put("isuse","");
                        if(good!=null&&good.getCategory_id()!=null&&good.getCategory_id().contains(cp.getId()+"")){
                            childmap.put("isselected","selected");
                        }
                        returnlist.add(childmap);
                    }
                }
            }
        }

        model.addAttribute("categoryList",returnlist);
        return new ModelAndView("/good/editgoods");
    }

    /**
     * 添加商品页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("addpage")
    public ModelAndView addpage(HttpServletRequest request,Model model){
        List<GoodCategory> categoryList = goodCategoryService.selectAll();
        List<Map<String,String>> returnlist = new ArrayList<>();
        for(GoodCategory g:categoryList){
            if(g.getParent_id()==0){
                Map map = new HashMap();
                map.put("id",g.getId());
                map.put("name",g.getName());
                map.put("isuse","disabled");
                returnlist.add(map);
                for(GoodCategory cp:categoryList){
                    if(cp.getParent_id()==g.getId()){
                        Map childmap = new HashMap();
                        childmap.put("id",cp.getId());
                        childmap.put("name",cp.getName());
                        childmap.put("isuse","");
                        returnlist.add(childmap);
                    }
                }
            }
        }
        model.addAttribute("categoryList",returnlist);
        return new ModelAndView("/good/addgoods");
    }
    /**
     * 删除方法
     * @param request
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public JsonResult delete(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            if(id!=null){
                goodService.delete(id);
            }else{
                return new JsonResult(JsonResult.ERROR,"参数异常");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }
    /**
     * 删除方法
     * @param request
     * @return
     */
    @RequestMapping("delSpec")
    @ResponseBody
    public JsonResult delSpec(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            if(id!=null){
                goodSpecService.delete(id);
            }else{
                return new JsonResult(JsonResult.ERROR,"参数异常");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }
    /**
     * 删除方法
     * @param request
     * @return
     */
    @RequestMapping("delSpecItem")
    @ResponseBody
    public JsonResult delSpecItem(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        try {
            if(id!=null){
                goodSpecitemService.delete(id);
            }else{
                return new JsonResult(JsonResult.ERROR,"参数异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 添加商品方法
     * @param request
     * @return
     */
    @RequestMapping("insert")
    @ResponseBody
    public JsonResult insert(HttpServletRequest request){
        String title = StringUtils.isEmpty(request.getParameter("title"))?null:request.getParameter("title");
        String subtitle = StringUtils.isEmpty(request.getParameter("subtitle"))?null:request.getParameter("subtitle");
        String keyword = StringUtils.isEmpty(request.getParameter("keyword"))?null:request.getParameter("keyword");
        String category_id = StringUtils.isEmpty(request.getParameter("category_id"))?null:request.getParameter("category_id");
        String thumb = StringUtils.isEmpty(request.getParameter("thumb"))?null:request.getParameter("thumb");
        String thumbs = StringUtils.isEmpty(request.getParameter("thumbs"))?null:request.getParameter("thumbs");
        String marketprice = StringUtils.isEmpty(request.getParameter("marketprice"))?null:request.getParameter("marketprice");
        String productprice = StringUtils.isEmpty(request.getParameter("productprice"))?null:request.getParameter("productprice");
        String stock = StringUtils.isEmpty(request.getParameter("stock"))?null:request.getParameter("stock");
        String expressprice = StringUtils.isEmpty(request.getParameter("expressprice"))?null:request.getParameter("expressprice");
        String description = StringUtils.isEmpty(request.getParameter("description"))?null:request.getParameter("description");
        String state = StringUtils.isEmpty(request.getParameter("state"))?null:request.getParameter("state");
        String sort = StringUtils.isEmpty(request.getParameter("sort"))?null:request.getParameter("sort");
        String type = StringUtils.isEmpty(request.getParameter("type"))?"0":request.getParameter("type");
        String startprice = StringUtils.isEmpty(request.getParameter("startprice"))?"0":request.getParameter("startprice");
        String stepprice = StringUtils.isEmpty(request.getParameter("stepprice"))?"0":request.getParameter("stepprice");
        String auction_start_time = StringUtils.isEmpty(request.getParameter("auction_start_time"))?null:request.getParameter("auction_start_time");
        String auction_end_time = StringUtils.isEmpty(request.getParameter("auction_end_time"))?null:request.getParameter("auction_end_time");
        try {
            if(stock==null||productprice==null||marketprice==null||state==null||sort==null){
                return new JsonResult(JsonResult.ERROR,"参数异常");
            }
            //获取直营店铺
            LmMerchInfo lmMerchInfo = lmMerchInfoService.findDirectMerch();
            Good good = new Good();
            good.setTitle(title);
            good.setSubtitle(subtitle);
            good.setKeyword(keyword);
            good.setCategory_id(category_id);
            good.setThumb(thumb);
            good.setStock(Integer.parseInt(stock));
            good.setThumbs(thumbs);
            good.setProductprice(new BigDecimal(productprice));
            good.setMarketprice(new BigDecimal(marketprice));
            good.setExpressprice(new BigDecimal(expressprice));
            good.setDescription(description);
            good.setState(Integer.parseInt(state));
            good.setSort(Integer.parseInt(sort));
            good.setType(Integer.parseInt(type));
            good.setCost_price(new BigDecimal(0));
            good.setCreate_at(new Date());
            good.setUpdate_at(new Date());
            if(lmMerchInfo!=null){
                good.setMer_id(lmMerchInfo.getId());
                good.setMer_use(lmMerchInfo.getMember_id());
            }
            if("1".equals(type)){
                good.setAuction_start_time(DateUtils.sdf_yMdHms.parse(auction_start_time));
                good.setAuction_end_time(DateUtils.sdf_yMdHms.parse(auction_end_time));
                good.setStartprice(new BigDecimal(startprice));
                good.setStepprice(new BigDecimal(stepprice));
            }
            goodService.insertService(good);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 修改商品信息
     * @param request
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    @Transactional
    public JsonResult edit(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        String title = StringUtils.isEmpty(request.getParameter("title"))?null:request.getParameter("title");
        String subtitle = StringUtils.isEmpty(request.getParameter("subtitle"))?null:request.getParameter("subtitle");
        String keyword = StringUtils.isEmpty(request.getParameter("keyword"))?null:request.getParameter("keyword");
        String category_id = StringUtils.isEmpty(request.getParameter("category_id"))?null:request.getParameter("category_id");
        String thumb = StringUtils.isEmpty(request.getParameter("thumb"))?null:request.getParameter("thumb");
        String thumbs = StringUtils.isEmpty(request.getParameter("thumbs"))?null:request.getParameter("thumbs");
        String marketprice = StringUtils.isEmpty(request.getParameter("marketprice"))?null:request.getParameter("marketprice");
        String productprice = StringUtils.isEmpty(request.getParameter("productprice"))?null:request.getParameter("productprice");
        String stock = StringUtils.isEmpty(request.getParameter("stock"))?null:request.getParameter("stock");
        String expressprice = StringUtils.isEmpty(request.getParameter("expressprice"))?null:request.getParameter("expressprice");
        String description = StringUtils.isEmpty(request.getParameter("description"))?null:request.getParameter("description");
        String state = StringUtils.isEmpty(request.getParameter("state"))?null:request.getParameter("state");
        String sort = StringUtils.isEmpty(request.getParameter("sort"))?null:request.getParameter("sort");
        String type = StringUtils.isEmpty(request.getParameter("type"))?"0":request.getParameter("type");
        String startprice = StringUtils.isEmpty(request.getParameter("startprice"))?"0":request.getParameter("startprice");
        String stepprice = StringUtils.isEmpty(request.getParameter("stepprice"))?"0":request.getParameter("stepprice");
        String auction_start_time = StringUtils.isEmpty(request.getParameter("auction_start_time"))?null:request.getParameter("auction_start_time");
        String auction_end_time = StringUtils.isEmpty(request.getParameter("auction_end_time"))?null:request.getParameter("auction_end_time");

//        String[] spec_ids = request.getParameterValues("spec_id");
//        String[] spec_titles = request.getParameterValues("spec_title");
            if(id==null){
                return new JsonResult(JsonResult.ERROR,"参数异常");
            }
        try {
            //更改商品信息
            Good good = goodService.findById(Integer.parseInt(id));
            good.setTitle(title);
            good.setSubtitle(subtitle);
            good.setKeyword(keyword);
            good.setCategory_id(category_id);
            good.setThumb(thumb);
            good.setThumbs(thumbs);
            good.setStock(Integer.parseInt(stock));
            good.setProductprice(new BigDecimal(productprice));
            good.setMarketprice(new BigDecimal(marketprice));
            good.setExpressprice(new BigDecimal(expressprice));
            good.setDescription(description);
            good.setState(Integer.parseInt(state));
            good.setUpdate_at(new Date());
            good.setSort(Integer.parseInt(sort));
            good.setType(Integer.parseInt(type));
            if("1".equals(type)){
                good.setAuction_start_time(DateUtils.sdf_yMdHms.parse(auction_start_time));
                good.setAuction_end_time(DateUtils.sdf_yMdHms.parse(auction_end_time));
                good.setStartprice(new BigDecimal(startprice));
                good.setStepprice(new BigDecimal(stepprice));
            }
            goodService.updateGoods(good);
//            if("0".equals(type)){
//                //添加商品规格
//                for(int i=0;i<spec_ids.length;i++){
//                    if(StringUtils.isEmpty(spec_ids[i])){stock
//                        if(!StringUtils.isEmpty(spec_titles[i])){
//                            GoodSpec goodSpec = new GoodSpec();
//                            goodSpec.setCreated_at(new Date());
//                            goodSpec.setGoods_id(Integer.parseInt(id));
//                            goodSpec.setTitle(spec_titles[i]);
//                            goodSpec.setUpdated_at(new Date());
//                            goodSpecService.insertGoodspec(goodSpec);
//                            String [] spec_item_titles = request.getParameterValues("spec_item_title_"+spec_titles[i]);
//                            if(spec_item_titles!=null){
//                                for(String item_title:spec_item_titles){
//                                    GoodSpecItem goodSpecItem = new GoodSpecItem();
//                                    goodSpecItem.setCreated_at(new Date());
//                                    goodSpecItem.setGoods_id(Integer.parseInt(id));
//                                    goodSpecItem.setSpec_id(goodSpec.getId());
//                                    goodSpecItem.setTitle(item_title);
//                                    goodSpecitemService.insertGoodSpecItem(goodSpecItem);
//                                }
//                            }
//                        }
//                    }else{
//                        GoodSpec goodSpec = goodSpecService.findByid(spec_ids[i]);
//                        goodSpec.setUpdated_at(new Date());
//                        goodSpec.setTitle(spec_titles[i]);
//                        goodSpecService.updateGoodspec(goodSpec);
//                        String [] spec_item_id = request.getParameterValues("spec_item_id_"+spec_ids[i]);
//                        String [] spec_item_title = request.getParameterValues("spec_item_title_"+spec_titles[i]);
//
//                        for(int j=0;j<spec_item_id.length;j++){
//                            if(StringUtils.isEmpty(spec_item_id[j])){
//                                GoodSpecItem goodSpecItem = new GoodSpecItem();
//                                goodSpecItem.setCreated_at(new Date());
//                                goodSpecItem.setGoods_id(Integer.parseInt(id));
//                                goodSpecItem.setSpec_id(goodSpec.getId());
//                                goodSpecItem.setUpdated_at(new Date());
//                                goodSpecItem.setTitle(spec_item_title[j]);
//                                goodSpecitemService.insertGoodSpecItem(goodSpecItem);
//                            }else{
//                                GoodSpecItem goodSpecItem = goodSpecitemService.findById(spec_item_id[j]);
//                                goodSpecItem.setTitle(spec_item_title[j]);
//                                goodSpecItem.setUpdated_at(new Date());
//                                goodSpecitemService.updateGoodSpecItem(goodSpecItem);
//                            }
//                        }
//                    }
//
//                }
//                //添加选项
//
//                String[] option_stock = request.getParameterValues("option_stock");
//                String[] option_marketprice = request.getParameterValues("option_marketprice");
//                String[] option_productprice = request.getParameterValues("option_productprice");
//                String[] option_costprice = request.getParameterValues("option_costprice");
//                List<List<String>> list = new ArrayList<List<String>>();
//                List<List<String>> list2 = new ArrayList<List<String>>();
//                //删除原有关系
//                goodSpecOptionService.deleteByGoodid(id);
//                List<GoodSpec> goodSpecList = goodSpecService.findbyGoodId(id);
//                for(GoodSpec goodSpec:goodSpecList){
//                    List<GoodSpecItem> goodSpecItemList = goodSpecitemService.findBySpecid(goodSpec.getId());
//                    List<String> titlelist = new ArrayList<>();
//                    List<String> idlst = new ArrayList<>();
//                    for(GoodSpecItem goodSpecItem:goodSpecItemList){
//                        titlelist.add(goodSpecItem.getTitle());
//                        idlst.add(goodSpecItem.getId()+"");
//                    }
//                    list.add(titlelist);
//                    list2.add(idlst);
//                }
//                List<List<String>> titleresult = new ArrayList<List<String>>();
//                descartes(list, titleresult, 0, new ArrayList<String>());
//                List<List<String>> idresult = new ArrayList<List<String>>();
//                descartes(list2, idresult, 0, new ArrayList<String>());
//
//                //创建新的关系
//                for(int i=0;i<idresult.size();i++){
//                    boolean isactive = false;
//                    List<String> namelist =  titleresult.get(i);
//                    String titlename ="";
//                    for(String name:namelist){
//                        titlename+=name+",";
//                    }
//                    List<String> idlist =  idresult.get(i);
//                    String itemid ="";
//                    for(String itid:idlist){
//                        itemid+=itid+",";
//                    }
//                    GoodSpecOption goodSpecOption = new GoodSpecOption();
//                    goodSpecOption.setProductprice(new BigDecimal(option_productprice[i]));
//                    goodSpecOption.setMarketprice(new BigDecimal(option_marketprice[i]));
//                    goodSpecOption.setCostprice(new BigDecimal(option_costprice[i]));
//                    goodSpecOption.setGoods_id(Integer.parseInt(id));
//                    goodSpecOption.setStock(Integer.parseInt(option_stock[i]));
//                    goodSpecOption.setCreated_at(new Date());
//                    goodSpecOption.setUpdated_at(new Date());
//                    goodSpecOption.setTitle(titlename.substring(0,titlename.length()-1));
//                    goodSpecOption.setSpec_item_ids(itemid.substring(0,itemid.length()-1));
//                    goodSpecOptionService.insertService(goodSpecOption);
//                }
//            }else{
//                return new JsonResult(JsonResult.ERROR,"拍卖商品不支持规格项");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new JsonResult(JsonResult.ERROR,"参数异常");
        }
        return new JsonResult();
    }

    /**
     * 上下架
     * @param request
     * @return
     */
    @RequestMapping("changestatus")
    @ResponseBody
    @Transactional
    public JsonResult changestatus(HttpServletRequest request){
        String id = StringUtils.isEmpty(request.getParameter("id"))?null:request.getParameter("id");
        if(id==null){
            return new JsonResult(JsonResult.ERROR,"参数异常");
        }
        try {
            //更改商品信息
            Good good = goodService.findById(Integer.parseInt(id));
            if(good.getState()==0){
                good.setState(1);
            }else{
                good.setState(0);
            }
            goodService.updateGoods(good);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new JsonResult(JsonResult.ERROR,"参数异常");
        }
        return new JsonResult();
    }


    /**
     * 批量处理操作
     * @param request
     * @return
     */
    @RequestMapping("batchoption")
    @ResponseBody
    @Transactional
    public JsonResult batchoption(HttpServletRequest request){
        String ids = StringUtils.isEmpty(request.getParameter("ids"))?null:request.getParameter("ids");
        String operate = StringUtils.isEmpty(request.getParameter("operate"))?null:request.getParameter("operate");
        String categoryid = StringUtils.isEmpty(request.getParameter("categoryid"))?null:request.getParameter("categoryid");

        try {
            //批量删除
            if("delete".equals(operate)){
               String [] goodids = ids.split(",");
               for(String id:goodids){
                   goodService.delete(id);
               }
            }
            //批量上架
            if("putaway".equals(operate)){
                String [] goodids = ids.split(",");
                for(String id:goodids){
                    Good good = goodService.findById(Integer.parseInt(id));
                    good.setState(Good.statetwo);
                    goodService.updateGoods(good);
                }
            }
            //批量下架
            if("sold".equals(operate)){
                String [] goodids = ids.split(",");
                for(String id:goodids){
                    Good good = goodService.findById(Integer.parseInt(id));
                    good.setState(Good.stateone);
                    goodService.updateGoods(good);
                }
            }
            //批量更改分类
            if("edit".equals(operate)){
                String [] goodids = ids.split(",");
                for(String id:goodids){
                    Good good = goodService.findById(Integer.parseInt(id));
                    good.setCategory_id(categoryid);
                    goodService.updateGoods(good);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new JsonResult(e);
        }
        return new JsonResult();
    }


    /**
     * 批量修改分类页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("editcategorypage")
    public ModelAndView editcategorypage(HttpServletRequest request,Model model){
        String ids = StringUtils.isEmpty(request.getParameter("ids"))?null:request.getParameter("ids");
        List<GoodCategory> categoryList = goodCategoryService.selectAll();
        List<Map<String,String>> returnlist = new ArrayList<>();
        for(GoodCategory g:categoryList){
            if(g.getParent_id()==0){
                Map map = new HashMap();
                map.put("id",g.getId());
                map.put("name",g.getName());
                map.put("isuse","disabled");
                returnlist.add(map);
                for(GoodCategory cp:categoryList){
                    if(cp.getParent_id()==g.getId()){
                        Map childmap = new HashMap();
                        childmap.put("id",cp.getId());
                        childmap.put("name",cp.getName());
                        childmap.put("isuse","");
                        returnlist.add(childmap);
                    }
                }
            }
        }
        model.addAttribute("ids",ids);
        model.addAttribute("categoryList",returnlist);
        return new ModelAndView("/good/editcategory");
    }




    /**
     * 商品查询页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("selectgood")
    public ModelAndView selectgood(HttpServletRequest request, Model model){
        String title = StringUtils.isEmpty(request.getParameter("title"))?null:request.getParameter("title");
        String page = StringUtils.isEmpty(request.getParameter("page"))?"1":request.getParameter("page");
        String pagesize = StringUtils.isEmpty(request.getParameter("pagesize"))?"20":request.getParameter("pagesize");
        Map<String,String> condient = new HashMap<>(16);
        condient.put("title",title);
        condient.put("page",page);
        condient.put("pagesize",pagesize);
        List<Map> list = goodService.findListByCondient(condient);
        //参数
        model.addAttribute("condient",condient);
        //总数
        model.addAttribute("totalsize",list.size());
        //商品信息
        model.addAttribute("returninfo", PageUtil.startPage(list,Integer.parseInt(page),Integer.parseInt(pagesize)));
        //总页数
        model.addAttribute("totalpage",list.size()/Integer.parseInt(pagesize)+1);
        return new ModelAndView("/good/selectgoods");
    }







    private static void descartes(List<List<String>> dimvalue, List<List<String>> result, int layer, List<String> curList) {
        if (layer < dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                descartes(dimvalue, result, layer + 1, curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    descartes(dimvalue, result, layer + 1, list);
                }
            }
        } else if (layer == dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                result.add(curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    result.add(list);
                }
            }
        }
    }
}
