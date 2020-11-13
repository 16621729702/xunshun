package com.wink.livemall.admin.api.shop;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveSetCommands.SRemCommand;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.VerifyFields;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.MerchGoodService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberLog;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.order.dto.LmExpress;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderRefundLog;
import com.wink.livemall.order.service.LmMerchOrderService;
import com.wink.livemall.order.service.LmOrderExpressService;
import com.wink.livemall.order.service.LmOrderService;

@Controller
@RequestMapping("merchauction")
public class MerchAuctionController {

	Logger logger = LogManager.getLogger(GoodController.class);

	@Autowired
	private MerchGoodService merchGoodService;
	
	@Autowired
	private GoodService goodService;

	/**
	 * 拍品列表
	 * status 竞拍状态1竞拍中 2已截拍 3已流拍 4失效 不传值为所有拍品
	 * @return
	 */
	@RequestMapping("get_goods_list")
	@ResponseBody
	public JsonResult findPage(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if(StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户id为空");
			return jsonResult;
		}
		try {
			Map<String, String> params=new HashMap<String, String>();
			params.put("pageindex", request.getParameter("pageindex"));
			params.put("pagesize", request.getParameter("pagesize"));
			params.put("merchid", request.getParameter("merchid"));
			params.put("type","1");
			params.put("status", request.getParameter("status"));
			
			String type=request.getParameter("type");
			
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Map<String, Object>> list=merchGoodService.getPage(params);
			List<Map<String, Object>> list_=new ArrayList<Map<String,Object>>();
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					Map< String, Object> m=list.get(i);
//					Map<String, Object> obj=new HashMap<String, Object>();
					m.put("auction_start_time", sf.format((Date)m.get("auction_start_time")));
					m.put("auction_end_time", sf.format((Date)m.get("auction_end_time")));
					
					LmGoodAuction auction=merchGoodService.findLastAuction(Integer.parseInt(m.get("id")+""));
					if(auction==null) {
						m.put("curr_price",m.get("startprice"));
					}else {
						m.put("curr_price",auction.getPrice());
					}
					list_.add(m);
				}
				
			}
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("pageindex", request.getParameter("pageindex"));
			res.put("pagesize", request.getParameter("pagesize"));
			res.put("list", list_);
			jsonResult.setData(res);
			
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}
	
	
	/**
	 * 上下架拍品
	 * @param request id 商品id type 1上架 2下架
	 * @return
	 */
	@RequestMapping("pull_goods")
	@ResponseBody
	public JsonResult delGoods(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if(StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id为空");
			return jsonResult;
		}
		if(StringUtils.isEmpty(request.getParameter("type"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("type为空");
			return jsonResult;
		}
		try {
			
			Good good=goodService.findById(Integer.parseInt(request.getParameter("id")));
			if(good==null) {
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("商品不存在");
				return jsonResult;
			}
			if(request.getParameter("type").equals("1")) {
				good.setState(1);
			}else if(request.getParameter("type").equals("2")){
				good.setState(0);
			}
			
			merchGoodService.updateEntity(good);
			jsonResult.setCode(JsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * @return 统计竞拍中商品
	 */
	@RequestMapping("count_auction_goods")
	@ResponseBody
	public JsonResult countAuctionGoods(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		String merchid = request.getParameter("merchid");
		if (merchid == null) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("merchid为空");
			return jsonResult;
		}
		try {
			int num=merchGoodService.countAuctionGoods(Integer.parseInt(merchid));
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("num",num);
			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		
		return jsonResult;
	}


}
