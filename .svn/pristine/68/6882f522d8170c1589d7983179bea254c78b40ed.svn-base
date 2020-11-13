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

import com.google.common.collect.Maps;
import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.admin.util.DateUtils;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.VerifyFields;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LmGoodAuction;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.MerchGoodService;
import com.wink.livemall.live.dto.LmLiveLog;
import com.wink.livemall.live.service.LmLiveLogService;
import com.wink.livemall.live.service.LmMerchLiveService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberLog;
import com.wink.livemall.member.service.LmMemberFollowService;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchFollowService;
import com.wink.livemall.order.dto.LmExpress;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderRefundLog;
import com.wink.livemall.order.service.LmMerchOrderService;
import com.wink.livemall.order.service.LmOrderExpressService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.video.dto.LmVideoCategoary;
import com.wink.livemall.video.dto.LmVideoCore;
import com.wink.livemall.video.service.LmVideoCategoaryService;
import com.wink.livemall.video.service.LmVideoCoreService;

@Controller
@RequestMapping("merchreport")
public class MerchReportController {

	Logger logger = LogManager.getLogger(GoodController.class);


	@Autowired
	private LmMerchOrderService lmMerchOrderService;
	
	@Autowired
	private LmLiveLogService liveLogService;
	
	@Autowired
	private LmMemberFollowService lmMemberFollowService;

	/**
	 * 资金报表
	 * 成拍 金额 数量
	 * 销售 付款 数量 退款 数量
	 * 收款 金额 数量
	 * @param request
	 * @return
	 */
	@RequestMapping("get_finance_report")
	@ResponseBody
	public JsonResult getVideoCate(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("merchid为空");
			return jsonResult;
		}
		try {
			String merchid=request.getParameter("merchid");
			Map<String,Object> ok=lmMerchOrderService.staticOrderOk(merchid);
			Map<String,Object> pay=lmMerchOrderService.staticOrderPay(merchid);
			Map<String,Object> refund=lmMerchOrderService.staticOrderRefund(merchid);
			Map<String,Object> earn=lmMerchOrderService.staticOrderEarn(merchid);
			
			jsonResult.setCode(jsonResult.SUCCESS);
			Map<String, Object> res=new HashMap<String, Object>();
			res.put("ok", ok);
			res.put("pay", pay);
			res.put("refund", refund);
			res.put("earn", earn);
			jsonResult.setData(res);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}
	
	/**
	 * 成拍列表
	 * @return
	 */
	@RequestMapping("get_ok_list")
	@ResponseBody
	public JsonResult orderOkList(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户id为空");
			return jsonResult;
		}
		String pagesize = request.getParameter("pagesize");
		String pageindex = request.getParameter("pageindex");
		String merchid = request.getParameter("merchid");
		pagesize = StringUtils.isEmpty(pagesize) ? "0" : pagesize;
		pageindex = StringUtils.isEmpty(pageindex) ? "0" : pageindex;
		merchid = StringUtils.isEmpty(merchid) ? "0" : merchid;

		Map<String, String> params = new HashMap<>();
		params.put("pagesize", pagesize);
		params.put("pageindex", pageindex);
		params.put("merchid", merchid);
		params.put("type", request.getParameter("type"));

		try {
			List<Map<String, Object>> list = lmMerchOrderService.orderOkList(params);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("pageindex", pageindex);
			res.put("pagesize", pagesize);
			res.put("list", list);
			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}
	
	/**
	 * 销售 付款列表
	 * @return
	 */
	@RequestMapping("get_pay_list")
	@ResponseBody
	public JsonResult orderPayList(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户id为空");
			return jsonResult;
		}
		String pagesize = request.getParameter("pagesize");
		String pageindex = request.getParameter("pageindex");
		String merchid = request.getParameter("merchid");
		pagesize = StringUtils.isEmpty(pagesize) ? "0" : pagesize;
		pageindex = StringUtils.isEmpty(pageindex) ? "0" : pageindex;
		merchid = StringUtils.isEmpty(merchid) ? "0" : merchid;

		Map<String, String> params = new HashMap<>();
		params.put("pagesize", pagesize);
		params.put("pageindex", pageindex);
		params.put("merchid", merchid);
		params.put("type", request.getParameter("type"));

		try {
			List<Map<String, Object>> list = lmMerchOrderService.orderPayList(params);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("pageindex", pageindex);
			res.put("pagesize", pagesize);
			res.put("list", list);
			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}
	
	/**
	 * 销售 退款列表
	 * @return
	 */
	@RequestMapping("get_refund_list")
	@ResponseBody
	public JsonResult orderRefundList(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户id为空");
			return jsonResult;
		}
		String pagesize = request.getParameter("pagesize");
		String pageindex = request.getParameter("pageindex");
		String merchid = request.getParameter("merchid");
		pagesize = StringUtils.isEmpty(pagesize) ? "0" : pagesize;
		pageindex = StringUtils.isEmpty(pageindex) ? "0" : pageindex;
		merchid = StringUtils.isEmpty(merchid) ? "0" : merchid;

		Map<String, String> params = new HashMap<>();
		params.put("pagesize", pagesize);
		params.put("pageindex", pageindex);
		params.put("merchid", merchid);
		params.put("type", request.getParameter("type"));

		try {
			List<Map<String, Object>> list = lmMerchOrderService.orderRefundList(params);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("pageindex", pageindex);
			res.put("pagesize", pagesize);
			res.put("list", list);
			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}
	
	/**
	 * 收款列表
	 * @return
	 */
	@RequestMapping("get_earn_list")
	@ResponseBody
	public JsonResult orderEarnList(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户id为空");
			return jsonResult;
		}
		String pagesize = request.getParameter("pagesize");
		String pageindex = request.getParameter("pageindex");
		String merchid = request.getParameter("merchid");
		pagesize = StringUtils.isEmpty(pagesize) ? "0" : pagesize;
		pageindex = StringUtils.isEmpty(pageindex) ? "0" : pageindex;
		merchid = StringUtils.isEmpty(merchid) ? "0" : merchid;

		Map<String, String> params = new HashMap<>();
		params.put("pagesize", pagesize);
		params.put("pageindex", pageindex);
		params.put("merchid", merchid);
		params.put("type", request.getParameter("type"));

		try {
			List<Map<String, Object>> list = lmMerchOrderService.orderEarnList(params);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("pageindex", pageindex);
			res.put("pagesize", pagesize);
			res.put("list", list);
			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}
	
	
	/**
	 * 店铺报表
	 * @param request
	 * @return
	 */
	@RequestMapping("get_merch_report")
	@ResponseBody
	public JsonResult auditOrder(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户id为空");
			return jsonResult;
		}	
		try {
			Map<String, Object> res = new HashMap<String, Object>();
			Long seconds=liveLogService.countTime(request.getParameter("merchid"));
			Map<String, Object> live=lmMerchOrderService.staticLive(request.getParameter("merchid"));
			int follow=lmMemberFollowService.countNumYed(request.getParameter("merchid"));
			String time=DateUtils.getTimeZHCNBySeconds(seconds);
			res.put("time",	 time);
			res.put("live", live);
			res.put("follow", follow);
			jsonResult.setCode(jsonResult.SUCCESS);
			jsonResult.setData(res);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
		}
		
		return  jsonResult;
	}
	
	
	/**
	 * 直播列表
	 * @return
	 */
	@RequestMapping("get_live_list")
	@ResponseBody
	public JsonResult findLiveList(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户id为空");
			return jsonResult;
		}
		String pagesize = request.getParameter("pagesize");
		String pageindex = request.getParameter("pageindex");
		String merchid = request.getParameter("merchid");
		pagesize = StringUtils.isEmpty(pagesize) ? "0" : pagesize;
		pageindex = StringUtils.isEmpty(pageindex) ? "0" : pageindex;
		merchid = StringUtils.isEmpty(merchid) ? "0" : merchid;

		Map<String, String> params = new HashMap<>();
		params.put("pagesize", pagesize);
		params.put("pageindex", pageindex);
		params.put("merchid", merchid);
	
		try {
			List<LmLiveLog> list = liveLogService.findPage(params);
			Map<String, Object> res = new HashMap<String, Object>();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			res.put("pageindex", pageindex);
			res.put("pagesize", pagesize);
			List<Map<String, Object>> list_=new ArrayList<Map<String,Object>>();
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					LmLiveLog liveLog=list.get(i);
					Map<String, Object> m=new HashMap<String, Object>();
					m.put("starttime",sf.format(liveLog.getStarttime()));
					if(liveLog.getEndtime()!=null) {
						m.put("endtime",sf.format(liveLog.getEndtime()));
					}
					m.put("id",liveLog.getId());
					m.put("status",liveLog.getStatus());
					System.out.println(m);
					list_.add(m);
				}
			}
			res.put("list", list_);
			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}
	
	/**
	 * 直播订单列表
	 * @return
	 */
	@RequestMapping("get_live_order_list")
	@ResponseBody
	public JsonResult orderLiveList(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merchid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户id为空");
			return jsonResult;
		}
		String pagesize = request.getParameter("pagesize");
		String pageindex = request.getParameter("pageindex");
		String merchid = request.getParameter("merchid");
		pagesize = StringUtils.isEmpty(pagesize) ? "0" : pagesize;
		pageindex = StringUtils.isEmpty(pageindex) ? "0" : pageindex;
		merchid = StringUtils.isEmpty(merchid) ? "0" : merchid;

		Map<String, String> params = new HashMap<>();
		params.put("pagesize", pagesize);
		params.put("pageindex", pageindex);
		params.put("merchid", merchid);
		
		try {
			List<Map<String, Object>> list = lmMerchOrderService.orderLiveList(params);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("pageindex", pageindex);
			res.put("pagesize", pagesize);
			res.put("list", list);
			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}
	
}
