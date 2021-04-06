package com.wink.livemall.admin.api.shop;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wink.livemall.admin.util.Errors;
import com.wink.livemall.admin.util.filterUtils.CheckTextAPI;
import com.wink.livemall.admin.util.filterUtils.GetAuthService;
import com.wink.livemall.goods.dto.*;
import com.wink.livemall.goods.service.*;
import com.wink.livemall.goods.utils.HttpJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.VerifyFields;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.service.LmMerchOrderService;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "商户商品接口")
@RestController
@RequestMapping("merchgoods")
public class MerchGoodsController {

	Logger logger = LogManager.getLogger(MerchGoodsController.class);

	@Autowired
	private GoodCategoryService goodCategoryService;

	@Autowired
	private MerchWareHouseService merchWareHouseService;

	@Autowired
	private MerchGoodService merchGoodService;

	@Autowired
	private LmMerchInfoService lmMerchInfoService;

	@Autowired
	private GoodService goodService;

	@Autowired
	private LmMerchOrderService lmMerchOrderService;

	@Autowired
	private LmGoodAuctionService lmGoodAuctionService;


	/**
	 * 获取商品详情
	 * @param request
	 * @return
	 */
	@RequestMapping("get_goods_detail")
	@ResponseBody
	public JsonResult findGoodByid(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id为空");
			return jsonResult;
		}
		try {
			Map<String, Object> good=merchGoodService.findGoodByid((request.getParameter("id")));
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String auction_start_time_str="";
			String auction_end_time_str="";
			if(!StringUtils.isEmpty(good.get("auction_start_time"))) {
				auction_start_time_str=sf.format((Date)good.get("auction_start_time"));
			}
			if(!StringUtils.isEmpty(good.get("auction_end_time"))) {
				auction_end_time_str=sf.format((Date)good.get("auction_end_time"));
			}
			good.put("auction_start_time", auction_start_time_str);
			good.put("auction_end_time", auction_end_time_str);
			good.put("auction_start_time_str", auction_start_time_str);
			good.put("auction_end_time_str", auction_end_time_str);
			jsonResult.setCode(jsonResult.SUCCESS);
			jsonResult.setData(good);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return  jsonResult;
	}

	/**
	 * 搜索店铺商品
	 *
	 * @param request type 不传值所有商品 1一口价商品 2直播商品
	 * @return
	 */
	@RequestMapping("ser_goods")
	@ResponseBody
	public JsonResult getLiveGoods(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("merchid为空");
			return jsonResult;
		}
//		if (StringUtils.isEmpty(request.getParameter("keyword"))) {
//			jsonResult.setCode(jsonResult.ERROR);
//			jsonResult.setMsg("搜索内容为空");
//			return jsonResult;
//		}

		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("merchid", request.getParameter("merchid"));
			params.put("keyword", request.getParameter("keyword"));
			params.put("type", request.getParameter("type"));
			List<Map<String, Object>> list = merchGoodService.findListByCondition(params);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("list", list);
			jsonResult.setData(res);

			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

	/**
	 * @return 材质列表
	 */
	@RequestMapping("get_material_list")
	@ResponseBody
	public JsonResult getMaterialList(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();

		try {
			List<LmGoodMaterial> list = goodService.getGoodMaterialByApi();
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("list", list);
			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

	/**
	 * 获取商品库
	 */
	@RequestMapping("get_warehouse_list")
	@ResponseBody
	public JsonResult findGoodsCate() {
		JsonResult jsonResult = new JsonResult();

		try {
			List<WareHouse> list = merchWareHouseService.findList();
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("list", list);
			jsonResult.setData(res);
			jsonResult.setMsg(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

	/**
	 * 获取商品分类 返回二级分类集合
	 */
	@RequestMapping("get_goods_cate")
	@ResponseBody
	public JsonResult findGoodsWarehouse() {
		JsonResult jsonResult = new JsonResult();

		try {
			List<GoodCategory> list = goodCategoryService.findActiveList();
			List<Menu> menulist = new ArrayList<Menu>();

			List<Menu> menus = goodCategoryService.returnMenuTree(list, menulist);

			Map<String, Object> res = new HashMap<String, Object>();
			res.put("list", menus);
			jsonResult.setData(res);
			jsonResult.setMsg(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;

	}

	/**
	 * 添加商品
	 */
	@RequestMapping("add_goods")
	@ApiOperation(value = "商家发布商品",notes = "商家发布商品接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "goodVo", value = "接收商家填写信息对象", dataType = "Object",paramType = "query")
	})
	public HttpJsonResult addGoods(HttpServletRequest request,Good goodVo) {
		HttpJsonResult httpJsonResult =new HttpJsonResult();
		String access_token = GetAuthService.getAuth(CheckTextAPI.apiKey,CheckTextAPI.secretKey);
		CheckTextAPI checkTextAPI =new CheckTextAPI();
		String checkTitle="";
		if(!StringUtils.isEmpty(goodVo.getTitle())){
			checkTitle=checkTitle+goodVo.getTitle();
		}
		if(!StringUtils.isEmpty(goodVo.getDescription())){
			checkTitle=checkTitle+goodVo.getDescription();
		}
		if(!StringUtils.isEmpty(goodVo.getMaterial())){
			checkTitle=checkTitle+goodVo.getMaterial();
		}
		if(!StringUtils.isEmpty(goodVo.getSpec())){
			checkTitle=checkTitle+goodVo.getSpec();
		}
		if(!StringUtils.isEmpty(goodVo.getPlace())){
			checkTitle=checkTitle+goodVo.getPlace();
		}
		HttpJsonResult check = checkTextAPI.check(checkTitle, access_token);
		if(check.getCode()!=200){
			httpJsonResult.setCode(Errors.ERROR.getCode());
			httpJsonResult.setMsg(check.getMsg());
			return httpJsonResult;
		}
		httpJsonResult =merchGoodService.changeGood(goodVo,1);
		return httpJsonResult;
	}

	/**
	 * 修改商品
	 */
	@RequestMapping("upd_goods")
	@ApiOperation(value = "商家修改商品",notes = "商家修改商品接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "goodVo", value = "接收商家填写信息对象", dataType = "Object",paramType = "query")
	})
	public HttpJsonResult updGoods(HttpServletRequest request,Good goodVo) {
		HttpJsonResult httpJsonResult =new HttpJsonResult();
		String access_token = GetAuthService.getAuth(CheckTextAPI.apiKey,CheckTextAPI.secretKey);
		CheckTextAPI checkTextAPI =new CheckTextAPI();
		String checkTitle="";
		if(!StringUtils.isEmpty(goodVo.getTitle())){
			checkTitle=checkTitle+goodVo.getTitle();
		}
		if(!StringUtils.isEmpty(goodVo.getDescription())){
			checkTitle=checkTitle+goodVo.getDescription();
		}
		if(!StringUtils.isEmpty(goodVo.getMaterial())){
			checkTitle=checkTitle+goodVo.getMaterial();
		}
		if(!StringUtils.isEmpty(goodVo.getSpec())){
			checkTitle=checkTitle+goodVo.getSpec();
		}
		if(!StringUtils.isEmpty(goodVo.getPlace())){
			checkTitle=checkTitle+goodVo.getPlace();
		}
		HttpJsonResult check = checkTextAPI.check(checkTitle, access_token);
		if(check.getCode()!=200){
			httpJsonResult.setCode(Errors.ERROR.getCode());
			httpJsonResult.setMsg(check.getMsg());
			return httpJsonResult;
		}
		httpJsonResult =merchGoodService.changeGood(goodVo,2);
		return httpJsonResult;
	}

	/**
	 * 获取商品列表
	 */
	@RequestMapping("get_goods_list_merch")
	@ResponseBody
	public JsonResult getGoodsPage(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户id为空");
			return jsonResult;
		}
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("pageindex", request.getParameter("pageindex"));
			params.put("pagesize", request.getParameter("pagesize"));
			if (!StringUtils.isEmpty(request.getParameter("merchid"))) {
				params.put("merchid", request.getParameter("merchid"));
			}
			if (!StringUtils.isEmpty(request.getParameter("warehouse"))) {
				params.put("warehouse", request.getParameter("warehouse"));
			}
			if (!StringUtils.isEmpty(request.getParameter("type"))) {
				params.put("type", request.getParameter("type"));
			}
			if (!StringUtils.isEmpty(request.getParameter("category_id"))) {
				params.put("category_id", request.getParameter("category_id"));
			}
			if (!StringUtils.isEmpty(request.getParameter("sn"))) {
				params.put("sn", request.getParameter("sn"));
			}
			if (!StringUtils.isEmpty(request.getParameter("title"))) {
				params.put("title", request.getParameter("title"));
			}
			if (!StringUtils.isEmpty(request.getParameter("price_start"))) {
				params.put("price_start", request.getParameter("price_start"));
			}
			if (!StringUtils.isEmpty(request.getParameter("price_end"))) {
				params.put("price_end", request.getParameter("price_end"));
			}
			if (!StringUtils.isEmpty(request.getParameter("date"))) {
				params.put("date", request.getParameter("date"));
			}
			if (!StringUtils.isEmpty(request.getParameter("sort"))) {
				params.put("sort", request.getParameter("sort"));
			}

			List<Map<String, Object>> list = merchGoodService.getPage(params);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("pageindex", request.getParameter("pageindex"));
			res.put("pagesize", request.getParameter("pagesize"));

			if(list.size()>0) {
				SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> m=list.get(i);
					if( m.get("auction_start_time")!=null) {
						Date auction_start_time=(Date) m.get("auction_start_time");
						String auction_start_time_str=sf.format(auction_start_time);
						m.put("auction_start_time_str", auction_start_time_str);
					}else {
						m.put("auction_start_time_str", "");
					}
					if(m.get("auction_end_time")!=null) {
						Date auction_end_time=(Date) m.get("auction_end_time");
						String auction_end_time_str=sf.format(auction_end_time);
						m.put("auction_end_time_str", auction_end_time_str);
					}else {
						m.put("auction_end_time_str", "");
					}
					if(!StringUtils.isEmpty(m.get("warehouse"))){
						List<Map<String, Object>> w=merchGoodService.findWarehouses(m.get("warehouse")+"");
						String warehouse_str="";
						if(w.size()>0) {
							for (int j = 0; j < w.size(); j++) {
								Map<String, Object> s=w.get(j);
								warehouse_str+=s.get("name")+",";
							}
						}
						m.put("warehouse_str", warehouse_str.substring(0, warehouse_str.length()-1));
					}
				}
			}
			System.out.println(list.size());
			res.put("list", list);
			jsonResult.setData(res);

		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 删除商品
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("del_goods")
	@ResponseBody
	public JsonResult delGoods(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商品id为空");
			return jsonResult;
		}
		try {
			Good good=goodService.findById(Integer.parseInt(request.getParameter("id")));
			good.setState(0);
			good.setStock(0);
			good.setAuction_status(2);
			good.setIsdelete(1);
			merchGoodService.updateEntity(good);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 商品库信息
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("get_warehouse_info")
	@ResponseBody
	public JsonResult warehouseInfo(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户id为空");
			return jsonResult;
		}
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			int merchid = Integer.parseInt(request.getParameter("merchid"));
			LmMerchInfo info = lmMerchInfoService.findById(request.getParameter("merchid"));
			int goodsnum = merchGoodService.countGoodsNum(merchid);
			int ordernum = merchGoodService.countOrderNum(merchid);
			Map<String, Object> res1=lmMerchOrderService.staticOrderMonth(request.getParameter("merchid"), "1");
			Map<String, Object> res2=lmMerchOrderService.staticOrderMonth(request.getParameter("merchid"), "2");
//			int orderprice = merchGoodService.countOrderPrice(merchid) == null ? 0
//					: merchGoodService.countOrderPrice(merchid);
			int orderpay=lmMerchOrderService.countPay(request.getParameter("merchid"));
			int orderrefund=lmMerchOrderService.countRefund(request.getParameter("merchid"));
			map.put("info", info);
			map.put("goodsnum", goodsnum);
			map.put("orderpay", orderpay);
			map.put("orderrefund", orderrefund);
			map.put("orderprice_curr", res1.get("price"));
			map.put("orderprice_last", res2.get("price"));
			jsonResult.setCode(jsonResult.SUCCESS);
			jsonResult.setData(map);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 合买商品抽签
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("draw_lots")
	@ResponseBody
	public JsonResult auditOrder(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id为空");
			return jsonResult;
		}
		try {
			Good good=	goodService.findById(Integer.parseInt(request.getParameter("id")));
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}


	/**
	 * 商品上架下架 new
	 * @param request
	 * @return
	 */
	@RequestMapping("upd_good_state")
	@ResponseBody
	@ApiOperation(value = "商品上架下架",notes = "商品上架下架接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "商品id",required =true,  dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "type", value = "0是一口价 1是拍卖",required =true,  dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "state", value = "0 下架 1 上架",required =false,  dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "auctionStartTime", value = "开始时间",required =false,  dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "auctionEndTime", value = "结束时间",required =false,  dataType = "String",paramType = "query")
	})
	public JsonResult updGoodState(HttpServletRequest request,Integer[] id,Integer type,Integer state,String auctionStartTime,String auctionEndTime ) throws ParseException {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(jsonResult.SUCCESS);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i=0;i<id.length;i++){
			Integer ids = id[i];
			Good good=	goodService.findById(ids);
			good.setState(state);
			if(state==0){
				good.setWarehouse("1");
			}else {
				good.setWarehouse("2");
			}
			if(type==1){
				good.setStock(1);
				List<LmGoodAuction> list = lmGoodAuctionService.findAllByGoodid(ids.toString(), 0);
				if(list!=null&&list.size()>0){
					if(state==0){
						jsonResult.setMsg(good.getTitle()+"有竞拍记录，不可下架");
						jsonResult.setCode(jsonResult.ERROR);
						return jsonResult;
					}else {
						String materials="";
						GoodCategory goodCategory = goodCategoryService.findgoodscategoryById(good.getCategory_id());
						if(!StringUtils.isEmpty(goodCategory.getName())){
							for (int j = 0; j < 2; j++) {
								char word = goodCategory.getName().charAt(j);
								String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
								if (pinyinArray != null) {
									materials += Character.toUpperCase(pinyinArray[0].charAt(0));
								} else {
									materials += Character.toUpperCase(word);
								}
							}
						}
						Good goodDaoByIds = goodService.findById(good.getId());
						goodDaoByIds.setState(0);
						goodDaoByIds.setWarehouse("1");
						goodDaoByIds.setAuction_status(2);
						goodDaoByIds.setIsdelete(1);
						goodService.updateGoods(goodDaoByIds);
						good.setId(0);
						DateFormat format1 = new SimpleDateFormat("MMdd");//日期格式
						String sn = materials + String.format("%04d", good.getMer_id()) + format1.format(new Date()) + (int) (Math.random() * (9999 - 1000) + 1000);
						good.setSn(sn);
						good.setWarehouse("2");
						good.setAuction_status(0);
						good.setBidsnum(0);
						good.setIsdelete(0);
						good.setAuction_start_time(dateFormat.parse(auctionStartTime));
						good.setAuction_end_time(dateFormat.parse(auctionEndTime));
						goodService.insertService(good);
					}
				}else {
					if(state==0){
						good.setWarehouse("1");
						goodService.updateGoods(good);
					}else {
						good.setWarehouse("2");
						good.setAuction_start_time(dateFormat.parse(auctionStartTime));
						good.setAuction_end_time(dateFormat.parse(auctionEndTime));
						goodService.updateGoods(good);
					}
				}
			}else {
				if(state==1){
					if (good.getStock() < 1) {
						jsonResult.setMsg(good.getTitle() + "库存为0，不可改变状态，需要进入商品详情修改");
						jsonResult.setCode(jsonResult.ERROR);
						return jsonResult;
					}
				}
				goodService.updateGoods(good);
			}
		}
		return  jsonResult;
	}


	/**
	 * 商品库信息NEW
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("merGoodsInfo")
	@ResponseBody
	@ApiOperation(value = "商品库信息NEW",notes = "商品库信息NEW接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "merId", value = "店铺id",required =true,  dataType = "Integer",paramType = "query")
	})
	public JsonResult merGoodsInfo(HttpServletRequest request,Integer merId) {
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(jsonResult.SUCCESS);
		Map<String, Object> map = new HashMap<String, Object>();
		try{
		int fixedPutOn = merchGoodService.newCountGoodsNum(merId, 0, 1);//一口价上架商品
		int fixedPullOff = merchGoodService.newCountGoodsNum(merId,0,0);//一口价下架商品
		int auctionPutOn =merchGoodService.newCountGoodsNum(merId,1,1);//拍卖上架商品
		int auctionPutOff =merchGoodService.newCountGoodsNum(merId,1,0);//拍卖下架商品
		map.put("fixedPutOn", fixedPutOn);
		map.put("fixedPullOff", fixedPullOff);
		map.put("auctionPutOn", auctionPutOn);
		map.put("auctionPutOff", auctionPutOff);
		jsonResult.setCode(jsonResult.SUCCESS);
		jsonResult.setData(map);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return  jsonResult;
	}

}
