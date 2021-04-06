package com.wink.livemall.admin.api.shop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.wink.livemall.admin.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.live.dto.LmLiveLog;
import com.wink.livemall.live.service.LmLiveLogService;
import com.wink.livemall.member.service.LmMemberFollowService;
import com.wink.livemall.order.service.LmMerchOrderService;

@Api(tags = "报表接口")
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
			Map<String,Object> ok=lmMerchOrderService.staticOrderOk(merchid);//成拍
			Map<String,Object> pay=lmMerchOrderService.staticOrderPay(merchid);//已付款
			Map<String,Object> refund=lmMerchOrderService.staticOrderRefund(merchid,2);//已退款
			Map<String,Object> earn=lmMerchOrderService.staticOrderEarn(merchid,4);//已收款
			Map<String,Object> earnUnprocessed=lmMerchOrderService.staticOrderEarn(merchid,1);//未发货
			Map<String,Object> earnUndelivered=lmMerchOrderService.staticOrderEarn(merchid,2);//待确认
			Map<String,Object> applyRefund=lmMerchOrderService.staticOrderRefund(merchid,1);//退款中
			Map<String,Object> earnReturn=lmMerchOrderService.staticOrderEarn(merchid,3);//退货中
			jsonResult.setCode(jsonResult.SUCCESS);
			Map<String, Object> res=new HashMap<String, Object>();
			res.put("ok", ok);//成拍
			res.put("pay", pay);//已付款
			res.put("refund", refund);//已退款
			res.put("earn", earn);//已收款
			res.put("earnUnprocessed", earnUnprocessed);//未发货
			res.put("earnUndelivered", earnUndelivered);//待确认
			res.put("applyRefund", applyRefund);//退款中
			res.put("earnReturn", earnReturn);//退货中
			jsonResult.setData(res);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 直播报表
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "直播报表",notes = "直播报表接口")
	@RequestMapping("getMerLiveReport")
	@ResponseBody
	public JsonResult auditLiveOrder(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("merId"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户id为空");
			return jsonResult;
		}
		try {

			Map<String, Object> res = new HashMap<String, Object>();
			Long seconds=liveLogService.countTime(request.getParameter("merId"));
			Map<String, Object> live=lmMerchOrderService.staticLive(request.getParameter("merId"));
			int liveNum = lmMerchOrderService.staticLiveNum(request.getParameter("merId"));
			int follow=lmMemberFollowService.countNumYed(request.getParameter("merId"));
			String time=DateUtils.getTimeZHCNBySeconds(seconds);
			res.put("time",	 time);
			res.put("live", live);
			res.put("liveNum", liveNum);
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


	@RequestMapping("/orderLogList")
	@ResponseBody
	@ApiOperation(value = "资金报表列表",notes = "报表列表接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "merId", value = "商家Id", dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "type", value = "1 成拍 2 已付款 3 已退款 4  已收款 ", dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "startTime", value = "开始的时间", dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "entTime", value = "结尾时间", dataType = "String",paramType = "query")
	})
	private JsonResult orderLogList(HttpServletRequest request, String merId, Integer type, String startTime, String entTime) throws Exception {
		JsonResult jsonResult=new JsonResult();
		jsonResult.setCode(jsonResult.SUCCESS);
		try {
			Map<String, Object> orderLogList = lmMerchOrderService.orderLogList(merId, type, startTime, entTime);
			jsonResult.setData(orderLogList);
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}


	@RequestMapping("/auditLiveList")
	@ResponseBody
	@ApiOperation(value = "直播报表列表",notes = "直播报表列表接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "merId", value = "商家Id", dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "type", value = "1直播时长 2 直播销售", dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "startTime", value = "开始的时间", dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "entTime", value = "结尾时间", dataType = "String",paramType = "query")
	})
	private JsonResult auditLiveList(HttpServletRequest request, String merId, Integer type, String startTime, String entTime) throws Exception {
		JsonResult jsonResult=new JsonResult();
		jsonResult.setCode(jsonResult.SUCCESS);
		try {
			Map<String, Object> maps =null;
			if(type==1){
				maps= liveLogService.countTimeList(merId, startTime, entTime);
			}else if(type==2){
				maps=lmMerchOrderService.staticLiveList(merId, startTime, entTime);
			}
			jsonResult.setData(maps);
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	@RequestMapping("/staticLiveNumList")
	@ResponseBody
	@ApiOperation(value = "直播场数列表",notes = "直播场数列表接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "merId", value = "商家Id", dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "startTime", value = "开始的时间", dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "entTime", value = "结尾时间", dataType = "String",paramType = "query")
	})
	private JsonResult staticLiveNumList(HttpServletRequest request, String merId, String startTime, String entTime) throws Exception {
		JsonResult jsonResult=new JsonResult();
		jsonResult.setCode(jsonResult.SUCCESS);
		try {
			Map<String, Object> staticLiveNumList = liveLogService.liveNumList(merId, startTime, entTime);
			jsonResult.setData(staticLiveNumList);
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			logger.error(e.getMessage());
		}
		return jsonResult;
	}


	@RequestMapping("/dayLiveNumList")
	@ResponseBody
	@ApiOperation(value = "每日场数直播和订单列表",notes = "每日场数直播和订单列表接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "merId", value = "商家Id", dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "startTime", value = "开始的时间", dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "entTime", value = "结尾时间", dataType = "String",paramType = "query")
	})
	private JsonResult dayLiveNumList(HttpServletRequest request, String merId, String startTime, String entTime) throws Exception {
		JsonResult jsonResult=new JsonResult();
		jsonResult.setCode(jsonResult.SUCCESS);
		try {
			Map<String, Object> dayLiveNumList = liveLogService.dayLiveNumList(merId, startTime, entTime);
			jsonResult.setData(dayLiveNumList);
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
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
