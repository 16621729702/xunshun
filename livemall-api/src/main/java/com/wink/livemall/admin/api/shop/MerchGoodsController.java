package com.wink.livemall.admin.api.shop;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wink.livemall.goods.dto.*;
import com.wink.livemall.goods.service.*;
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

@Controller
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
	@ResponseBody
	public JsonResult addGoods(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		// 判断商户是否存在可用
		String mer_id = request.getParameter("mer_id");
		int fg = lmMerchInfoService.checkMerchEnable(mer_id);

		if (fg == 0) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户不存在或者被禁用");
			return jsonResult;
		}
		try {
			VerifyFields ver = new VerifyFields();
			String[] fields = { "weight", "mer_id", "type", "category_id", "place", "spec", "material", "title",
					"description", "marketprice", "stock",  "productprice", "freeshipping", "thumbs", "thumb","state"};
			Map<String, Object> res = ver.verifytoEntity(fields, request, "com.wink.livemall.goods.dto.Good");
			if ((int) res.get("error") == 0) {
				Good entity = (Good) res.get("entity");
				System.out.println(entity.toString());
//				if (!StringUtils.isEmpty(entity.getThumbs())) {
//					String[] arr = entity.getThumbs().split(",");
//					entity.setThumb(arr[0]);
//				}
				String material =request.getParameter("material");
				String materials="";
				for (int j = 0; j < 2; j++) {
					char word = material.charAt(j);
					String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
					if (pinyinArray != null) {
						materials += Character.toUpperCase(pinyinArray[0].charAt(0));
					} else {
						materials += Character.toUpperCase(word);
					}
				}
				if(entity.getState()==0){
					entity.setWarehouse("1");
				}else {
					DateFormat format1 = new SimpleDateFormat("MMdd");//日期格式
					Integer sns = Integer.parseInt(mer_id);
					String sn = materials + String.format("%04d", sns) + format1.format(new Date()) + (int) (Math.random() * (9999 - 1000) + 1000);
					entity.setSn(sn);
					entity.setWarehouse("2");
				}
				entity.setCreate_at(new Date());
				if(entity.getType()==1){
					if (!StringUtils.isEmpty(request.getParameter("startprice"))) {
						entity.setStartprice(new BigDecimal(request.getParameter("startprice")));
					}
					if (!StringUtils.isEmpty(request.getParameter("stepprice"))) {
						entity.setStepprice(new BigDecimal(request.getParameter("stepprice")));
					}
					if (!StringUtils.isEmpty(request.getParameter("delaytime"))) {
						entity.setDelaytime(Integer.parseInt(request.getParameter("delaytime")));
					}
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if (!StringUtils.isEmpty(request.getParameter("auction_start_time"))) {
						Date auction_start_time = sf.parse(request.getParameter("auction_start_time"));
						entity.setAuction_start_time(auction_start_time);
					}
					if (!StringUtils.isEmpty(request.getParameter("auction_end_time"))) {
						Date auction_end_time = sf.parse(request.getParameter("auction_end_time"));
						entity.setAuction_end_time(auction_end_time);
					}
				}
				if(entity.getType()==2){
					if (!StringUtils.isEmpty(request.getParameter("startprice"))) {
						entity.setStartprice(new BigDecimal(request.getParameter("startprice")));
					}
					if (!StringUtils.isEmpty(request.getParameter("stepprice"))) {
						entity.setStepprice(new BigDecimal(request.getParameter("stepprice")));
					}
					if (!StringUtils.isEmpty(request.getParameter("delaytime"))) {
						entity.setDelaytime(Integer.parseInt(request.getParameter("delaytime")));
					}
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if (!StringUtils.isEmpty(request.getParameter("auction_start_time"))) {
						Date auction_start_time = sf.parse(request.getParameter("auction_start_time"));
						entity.setAuction_start_time(auction_start_time);
					}
					if (!StringUtils.isEmpty(request.getParameter("auction_end_time"))) {
						Date auction_end_time = sf.parse(request.getParameter("auction_end_time"));
						entity.setAuction_end_time(auction_end_time);
					}
				}
				if (!StringUtils.isEmpty(request.getParameter("expressprice"))) {
					entity.setExpressprice(new BigDecimal(request.getParameter("expressprice")));
				}
				if (!StringUtils.isEmpty(request.getParameter("ischipped"))) {
					entity.setIschipped(Integer.parseInt(request.getParameter("ischipped")));
				}
				if (!StringUtils.isEmpty(request.getParameter("chipped_num"))) {
					entity.setChipped_num(Integer.parseInt(request.getParameter("chipped_num")));
				}
				if (!StringUtils.isEmpty(request.getParameter("chipped_price"))) {
					entity.setChipped_price(new BigDecimal(request.getParameter("chipped_price")));
				}
				entity.setState(1);
				merchGoodService.addGoods(entity);

				jsonResult.setCode(JsonResult.SUCCESS);
			} else {
				jsonResult.setMsg((String) res.get("msg"));
				jsonResult.setCode(JsonResult.ERROR);
			}

		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

	/**
	 * 修改商品
	 */
	@RequestMapping("upd_goods")
	@ResponseBody
	public JsonResult updGoods(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setMsg("id不能为空");
			jsonResult.setCode(JsonResult.ERROR);
			return jsonResult;
		}
		try {
			VerifyFields ver = new VerifyFields();
			String[] fields = { "weight",  "startprice", "stepprice","marketprice",
					"delaytime", "mer_id", "type", "category_id", "place", "spec", "material", "title", "description",
					"stock", "productprice", "freeshipping", "thumbs","thumb","expressprice" };
			List<Map<String, String>> field_ = ver.checkEmptyField(fields, request);
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String datestr = sf.format(date);
			Map<String, String> d = new HashMap<String, String>();
			d.put("field", "update_at");
			d.put("value", datestr);
			String status = request.getParameter("state");
			field_.add(d);
			Integer type=new Integer(request.getParameter("type"));
			if(type==1) {
				VerifyFields vers = new VerifyFields();
				String[] fieldes = { "weight", "mer_id", "type", "category_id", "place", "spec", "material", "title",
						"description", "marketprice", "stock",  "productprice", "freeshipping", "thumbs", "thumb" ,"state" };
				Map<String, Object> ress = vers.checkEmptytoEntity(fieldes, request, "com.wink.livemall.goods.dto.Good");
				String material =request.getParameter("material");
				String materials="";
				Good entityes = (Good) ress.get("entity");
				for (int j = 0; j < material.length(); j++) {
					char word = material.charAt(j);
					String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
					if (pinyinArray != null) {
						materials += Character.toUpperCase(pinyinArray[0].charAt(0));
					} else {
						materials += Character.toUpperCase(word);
					}
				}

				if (!StringUtils.isEmpty(request.getParameter("startprice"))) {
					entityes.setStartprice(new BigDecimal(request.getParameter("startprice")));
				}
				if (!StringUtils.isEmpty(request.getParameter("stepprice"))) {
					entityes.setStepprice(new BigDecimal(request.getParameter("stepprice")));
				}
				if (!StringUtils.isEmpty(request.getParameter("delaytime"))) {
					entityes.setDelaytime(Integer.parseInt(request.getParameter("delaytime")));
				}
				SimpleDateFormat sesf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if (!StringUtils.isEmpty(request.getParameter("auction_start_time"))) {
					Date auction_start_time = sesf.parse(request.getParameter("auction_start_time"));
					entityes.setAuction_start_time(auction_start_time);
				}
				if (!StringUtils.isEmpty(request.getParameter("auction_end_time"))) {
					Date auction_end_time = sesf.parse(request.getParameter("auction_end_time"));
					entityes.setAuction_end_time(auction_end_time);
				}
				entityes.setCreate_at(new Date());
				if(entityes.getState()==0){
					SimpleDateFormat times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date start_time = times.parse(request.getParameter("auction_start_time"));
					Date end_time = times.parse(request.getParameter("auction_end_time"));
					String auction_start_times = times.format(start_time);
					String auction_end_times = times.format(end_time);
					Map<String, String> state6 = new HashMap<String, String>();
					state6.put("field", "auction_start_time");
					state6.put("value", auction_start_times);
					field_.add(state6);
					Map<String, String> state5 = new HashMap<String, String>();
					state5.put("field", "auction_end_time");
					state5.put("value", auction_end_times);
					field_.add(state5);
					Map<String, String> state = new HashMap<String, String>();
					state.put("field", "state");
					state.put("value", "0");
					field_.add(state);
					Map<String, String> state2 = new HashMap<String, String>();
					state2.put("field", "auction_status");
					state2.put("value", "0");
					field_.add(state2);
					Map<String, String> state3 = new HashMap<String, String>();
					state3.put("field", "warehouse");
					state3.put("value", "1");
					field_.add(state3);
					merchGoodService.updateByFields(field_, request.getParameter("id"));
				}else {
					String mer_id = request.getParameter("mer_id");
					DateFormat format1 = new SimpleDateFormat("MMdd");//日期格式
					Integer sns = Integer.parseInt(mer_id);
					String sn = materials + String.format("%04d", sns) + format1.format(new Date()) + (int) (Math.random() * (9999 - 1000) + 1000);
					entityes.setSn(sn);
					entityes.setWarehouse("2");
					merchGoodService.addGoods(entityes);
					Good good=goodService.findById(Integer.parseInt(request.getParameter("id")));
					good.setState(0);
					good.setAuction_status(2);
					good.setIsdelete(1);
					merchGoodService.updateEntity(good);
				}
			}else {
				Map<String, String> state6 = new HashMap<String, String>();
				state6.put("field", "auction_start_time");
				state6.put("value", "null");
				field_.add(state6);
				Map<String, String> state5 = new HashMap<String, String>();
				state5.put("field", "auction_end_time");
				state5.put("value", "null");
				field_.add(state5);

				if (status != null && status.equals("1")) {
					//设置上架
					Map<String, String> state = new HashMap<String, String>();
					state.put("field", "state");
					state.put("value", "1");
					field_.add(state);
					Map<String, String> state2 = new HashMap<String, String>();
					state2.put("field", "auction_status");
					state2.put("value", "0");
					field_.add(state2);
					Map<String, String> state3 = new HashMap<String, String>();
					state3.put("field", "bidsnum");
					state3.put("value", "0");
					field_.add(state3);
					Map<String, String> state4 = new HashMap<String, String>();
					state4.put("field", "warehouse");
					state4.put("value", "2");
					field_.add(state4);

					List<LmGoodAuction> list = lmGoodAuctionService.findAllByGoodid(request.getParameter("id"), 0);
					if(null!=list){
						jsonResult.setMsg("该商品已有出价记录,不可修改成一口价商品");
						jsonResult.setCode(JsonResult.ERROR);
					}
					//清空出价记录
					/*for (LmGoodAuction lm : list) {
						lmGoodAuctionService.deleteService(lm);
					}*/
				} else {
					Map<String, String> state = new HashMap<String, String>();
					state.put("field", "state");
					state.put("value", "0");
					field_.add(state);
					Map<String, String> state4 = new HashMap<String, String>();
					state4.put("field", "warehouse");
					state4.put("value", "1");
					field_.add(state4);

					List<LmGoodAuction> list = lmGoodAuctionService.findAllByGoodid(request.getParameter("id"), 0);
					if(null!=list){
						jsonResult.setMsg("该商品已有出价记录,不可修改成一口价商品");
						jsonResult.setCode(JsonResult.ERROR);
					}
					//清空出价记录
					/*List<LmGoodAuction> list = lmGoodAuctionService.findAllByGoodid(request.getParameter("id"), 0);
					for (LmGoodAuction lm : list) {
						lmGoodAuctionService.deleteService(lm);
					}*/
				}

				merchGoodService.updateByFields(field_, request.getParameter("id"));
			}
			jsonResult.setCode(JsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
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

}
