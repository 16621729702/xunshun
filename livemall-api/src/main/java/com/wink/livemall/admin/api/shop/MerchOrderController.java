package com.wink.livemall.admin.api.shop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.admin.util.payUtil.PayUtil;
import com.wink.livemall.goods.dto.Good;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.goods.dto.LmShareGood;
import com.wink.livemall.merch.service.LmMerchInfoService;
import com.wink.livemall.order.dto.LmOrderLog;
import com.wink.livemall.order.service.LmOrderLogService;
import com.wink.livemall.sys.msg.service.PushmsgService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;

import net.sf.json.JSONObject;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveSetCommands.SRemCommand;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.admin.util.ExpressUtil;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.VerifyFields;
import com.wink.livemall.admin.util.WeixinpayUtil;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberLog;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.order.dto.LmExpress;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderRefundLog;
import com.wink.livemall.order.dto.LmPayLog;
import com.wink.livemall.order.service.LmMerchOrderService;
import com.wink.livemall.order.service.LmOrderExpressService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.order.service.LmPayLogService;

@Controller
@RequestMapping("merchorder")
public class MerchOrderController {

	Logger logger = LogManager.getLogger(MerchOrderController.class);

	@Autowired
	private LmMerchOrderService LmMerchOrderService;

	@Autowired
	private LmOrderService lmOrderService;

	@Autowired
	private LmOrderExpressService lmOrderExpressService;
	
	@Autowired
	private LmMemberService lmMemberService;
	@Autowired
	private GoodService goodService;
	@Autowired
	private LmMerchInfoService lmMerchInfoService;

	@Autowired
	private LmOrderLogService lmOrderLogService;
	
	
    @Autowired
    private LmPayLogService lmPayLogService;
    
    @Autowired
    private ConfigsService configsService;
    
    
	/**
	 * 订单列表
	 * 
	 * @return
	 */
	@RequestMapping("get_order_list_merch")
	@ResponseBody
	public JsonResult findPage(HttpServletRequest request) {
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
			List<Map<String, Object>> list = LmMerchOrderService.findPage(params);
			if(list!=null&&list.size()>0){
				for(Map<String,Object> map:list){
					System.out.println(map.toString());
					if(map.get("type")!=null){
						if((int)map.get("type")==3){
							//合买
							LmShareGood lmShareGood = goodService.findshareById((int)map.get("goodid"));
							map.put("goodname",lmShareGood.getName());
							map.put("thumb",lmShareGood.getImg());
							map.put("spec","");
						}else{
							if(map.get("islivegood")!=null&&(int)map.get("islivegood")==1){
								//直播商品
								LivedGood good = goodService.findLivedGood((int)map.get("goodid"));
								map.put("goodname",good.getName());
								map.put("thumb",good.getImg());
								map.put("spec","");
							}else{
								//普通订单
								Good good = goodService.findById((int)map.get("goodid"));
								if(good!=null){
									map.put("goodname",good.getTitle());
									map.put("thumb",good.getThumb());
									map.put("spec",good.getSpec());
								}
							}
						}
					}
				}
			}
			SimpleDateFormat sf=new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("pageindex", pageindex);
			res.put("pagesize", pagesize);
			res.put("list", list);
			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}
	
	
	/**
	 * 合买子订单列表
	 * 
	 * @return
	 */
	@RequestMapping("get_children_order_list")
	@ResponseBody
	public JsonResult findChildrenOrder(HttpServletRequest request ) {
		JsonResult jsonResult = new JsonResult();
		try {
			Map<String, String> params = new HashMap<>();
			params.put("pagesize", request.getParameter("pagesize"));
			params.put("pageindex", request.getParameter("pageindex"));
			params.put("porderid", request.getParameter("id"));
			List<Map<String, Object>> list = LmMerchOrderService.findChildrenOrder(params);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("list", list);
			jsonResult.setCode(jsonResult.SUCCESS);
			jsonResult.setData(res);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 获取商家订单长度
	 *
	 * @return
	 */
	@RequestMapping("get_order_size")
	@ResponseBody
	public JsonResult findOrderSize(HttpServletRequest request ) {
		JsonResult jsonResult = new JsonResult();
		String merchid = request.getParameter("merchid");
		merchid = StringUtils.isEmpty(merchid) ? "0" : merchid;
		Map<String, String> params = new HashMap<>();
		params.put("pagesize", "100000");
		params.put("pageindex", "1");
		params.put("merchid", merchid);
		try {
			Map<String,Object> res = new LinkedHashMap<>();
		for(int i=0;i<5;i++) {
			String type = String.valueOf(i);
			if(i==0){}else {
			params.put("type", type);
			}
			List<Map<String, Object>> list = LmMerchOrderService.findPage(params);
			if (i == 1){
				res.put("obligation_size", list.size());
		    }else if (i == 2){
				res.put("pending_size", list.size());
			}else if (i == 3){
				res.put("dispatched_size", list.size());
			}else if (i == 4) {
				res.put("post-sale_size", list.size());
			}else {
				res.put("total_size", list.size());
			}
		}
			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/** 废弃
	 * 获取合买子订单长度
	 *
	 * @return
	 */
	@RequestMapping("get_children_order_size")
	@ResponseBody
	public JsonResult findChildrenOrderSize(HttpServletRequest request ) {
		JsonResult jsonResult = new JsonResult();
		try {
			Map<String, String> params = new HashMap<>();
			params.put("porderid", request.getParameter("id"));


		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}



	/**
	 * 获取快递列表
	 * 
	 * @return
	 */
	@RequestMapping("get_express_list")
	@ResponseBody
	public JsonResult findExpressList() {
		JsonResult jsonResult = new JsonResult();
		try {
			System.out.println("express");
			List<LmExpress> list = lmOrderExpressService.getAll();
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("list", list);
			jsonResult.setCode(jsonResult.SUCCESS);
			jsonResult.setData(res);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 发货
	 * 
	 * @return
	 */
	@RequestMapping("send_order_merch")
	@ResponseBody
	public JsonResult sendOrder(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		VerifyFields ver = new VerifyFields();
		String[] fields = { "id", "expresssn", "expressid", "expressname", "express" };
		Map<String, Object> res = ver.verify(fields, request);

		if ((int) res.get("error") == 1) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg((String) res.get("msg"));
			return jsonResult;
		}
		LmOrder order = lmOrderService.findById(request.getParameter("id"));
		if (order == null) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("订单不存在");
			return jsonResult;
		}
		if (Integer.parseInt(order.getStatus()) > 1) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("已发货");
			return jsonResult;
		}

		try {
			String[] fields_arr = { "expresssn", "expressid", "expressname", "express" };
			List<Map<String, String>> fields_list = ver.checkEmptyField(fields_arr, request);
			Map<String, String> deliverytime = new HashMap<String, String>();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			deliverytime.put("field", "deliverytime");
			deliverytime.put("value", sf.format(new Date()));
			fields_list.add(deliverytime);
			Map<String, String> status = new HashMap<String, String>();
			status.put("field", "status");
			status.put("value", "2");
			fields_list.add(status);
			LmMerchOrderService.updateByFields(fields_list, request.getParameter("id"));
			jsonResult.setCode(jsonResult.SUCCESS);
			//pushmsgService.send(0,"商户已发货","2",order.getMemberid(),0);
			HttpClient httpClient = new HttpClient();
			String msg ="您订单号为:"+order.getOrderid()+"已发货\n"+"商户已打包成功,物流正在配送中,可及时查看您的物流信息";
			httpClient.send("交易消息",order.getMemberid()+"",msg);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 获取售后消息
	 * @param request
	 * @return
	 */
	@RequestMapping("get_refund_info")
	@ResponseBody
	public JsonResult getRefundInfo(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("orderid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("orderid为空");
			return jsonResult;
		}
		try {
			LmOrderRefundLog log=LmMerchOrderService.getRefundLogByorderid(request.getParameter("orderid"));
			if(log==null) {
				jsonResult.setCode(jsonResult.ERROR);
				jsonResult.setMsg("记录不存在");
				return jsonResult;
			}

			Map<String, Object> res=new HashMap<String, Object>();
			String[] imgs= {}; 
			if(!StringUtils.isEmpty(log.getImgs())) {
				imgs=log.getImgs().split(",");
			}
			res.put("imgs",imgs);
			res.put("money",StringUtils.isEmpty(log.getRefundmoney())?0:log.getRefundmoney());
			res.put("reason",log.getReason());
			res.put("type",log.getType());
			res.put("status",log.getStatus());
			res.put("express",StringUtils.isEmpty(log.getExpress())?"":log.getExpress());
			res.put("id", log.getId());
			res.put("text",StringUtils.isEmpty(log.getText())?"":log.getText());
			res.put("remark",StringUtils.isEmpty(log.getRemark())?"":log.getRemark());
			res.put("sn",StringUtils.isEmpty(log.getSn())?"":log.getSn());
			res.put("expressname",StringUtils.isEmpty(log.getExpressname())?"":log.getExpressname());
			jsonResult.setCode(jsonResult.SUCCESS);
			jsonResult.setData(res);
			
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		
		return jsonResult;
	}
	
	
	
	/**
	 * 售后处理 
	 *order type=1 退款 2退款退货 
	 *只退款情况退款申请确认后直接退款 order  backstatus=2 status=-1 ； refund status=2
	 *退款退货首先审核有效性  有效order  backstatus=1 ； refund status=1  
	 *打回order backstatus=0 refundid=0 ；refund status=-1 
	 *有效后客户回寄商品 收货后 完成order status=-1  backstatus=2 ； refund status=2 
	 * 需要判断订单的支付状态 paystatus 1余额 2银联
	 * 插入售后记录 确定后返回余额到客户账户
	 * @return
	 */
	@RequestMapping("audit_order_merch")
	@ResponseBody
	@Transactional
	public JsonResult auditOrder(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		VerifyFields ver = new VerifyFields();
		String[] fields = { "opt","refundid"};
		Map<String, Object> res = ver.verify(fields, request);
		if ((int) res.get("error") == 1) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg((String) res.get("msg"));
			return jsonResult;
		}
		LmOrderRefundLog refund=LmMerchOrderService.getRefundLog(Integer.parseInt(request.getParameter("refundid")));
 
		if(refund==null) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("售后申请不存在");
			return jsonResult;
		}
		
		/*
		 * if(refund.getStatus()==1) { // jsonResult.setCode(JsonResult.ERROR); //
		 * jsonResult.setMsg("退货退款请求已处理"); // return jsonResult; }
		 */
		if(refund.getStatus()==2) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("售后申请已处理");
			return jsonResult;
		}
		if(refund.getStatus()==-1) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("售后申请无效");
			return jsonResult;
		}
		if(refund.getStatus()==-2) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("售后申请已取消");
			return jsonResult;
		}
		LmOrder order = lmOrderService.findById(refund.getOrderid()+"");
		if (order == null) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("订单不存在");
			return jsonResult;
		}
		LmMerchInfo lmMerchInfo = lmMerchInfoService.findById(order.getMerchid()+"");
		if (lmMerchInfo == null) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("商户不存在");
			return jsonResult;
		}
		if (lmMerchInfo.getRefund_address() == null) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("没有添退货地址,请去设置,退货地址中添加");
			return jsonResult;
		}
		
		if (Integer.parseInt(order.getStatus()) == 0||Integer.parseInt(order.getStatus())==-1) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("订单状态错误");
			return jsonResult;
		}
		if (StringUtils.isEmpty(order.getBackstatus())||order.getBackstatus()==0) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("未申请售后");
			return jsonResult;
		}
		if (order.getBackstatus() == 2) {
			jsonResult.setCode(JsonResult.ERROR);
			jsonResult.setMsg("已处理");
			return jsonResult;
		}
		
		try {
			//打回
			if (request.getParameter("opt").equals("1")) {
				List<Map<String, String>> fields_list =new ArrayList<Map<String,String>>();
				Map<String, String> backstatus = new HashMap<String, String>();
				backstatus.put("field", "backstatus");
				backstatus.put("value", "0");
				fields_list.add(backstatus);
				LmMerchOrderService.updateByFields(fields_list,refund.getOrderid()+"");
				refund.setStatus(-1);
				refund.setChecktime(new Date());
				LmMerchOrderService.updRefundLog(refund);
				jsonResult.setCode(jsonResult.SUCCESS);
				
			}else if (request.getParameter("opt").equals("2")) {//确定
				
				LmOrder lmOrder = lmOrderService.findById(refund.getOrderid()+"");
				if(lmOrder.getStatus().equals("4")) {
					if(null==lmMerchInfo.getCredit() || lmMerchInfo.getCredit().compareTo(refund.getRefundmoney())==-1) 
					{
						jsonResult.setCode(JsonResult.ERROR);
						jsonResult.setMsg("账户余额不足，无法退款！");
					}
				}
				refund.setStatus(1);
				refund.setChecktime(new Date());
				LmMerchOrderService.updRefundLog(refund);
				jsonResult.setCode(jsonResult.SUCCESS);
				//商户发送信息给用户提示退款确认

				LmOrderLog lmOrderLog = new LmOrderLog();
				lmOrderLog.setOperate("退款确认");
				lmOrderLog.setOperatedate(new Date());
				lmOrderLog.setOrderid(refund.getOrderid()+"");
				lmOrderLogService.insert(lmOrderLog);
				
				
				//退回账户
			//	LmMerchInfo _lmMerchInfo = lmMerchInfoService.findById(refund.getMerchid()+"");
			//	_lmMerchInfo.setCredit(lmMerchInfo.getCredit().subtract(refund.getRefundmoney()));
			//	lmMerchInfoService.updateService(_lmMerchInfo);
				
//				pushmsgService.send(lmMerchInfo.getMember_id(),"退货退款请求已确认，请尽快填写物流信息","2",order.getMemberid(),1);
				HttpClient httpClient = new HttpClient();
				httpClient.send("交易消息",order.getMemberid()+"","退货退款请求已确认，请尽快填写物流信息");
			}else if(request.getParameter("opt").equals("3")){//完成
				
				LmOrder lmOrder = lmOrderService.findById(refund.getOrderid()+"");
				if(lmOrder.getStatus().equals("4")) {
					if(null==lmMerchInfo.getCredit() || lmMerchInfo.getCredit().compareTo(refund.getRefundmoney())==-1) 
					{
						jsonResult.setCode(JsonResult.ERROR);
						jsonResult.setMsg("账户余额不足，无法退款！");
					}
				}
//				List<Map<String, String>> fields_list =new ArrayList<Map<String,String>>();
//				Map<String, String> backstatus = new HashMap<String, String>();
//				backstatus.put("field", "backstatus");
//				backstatus.put("value", "2");
//				fields_list.add(backstatus);
//				Map<String, String> status = new HashMap<String, String>();
//				status.put("field", "status");
//				status.put("value", "-1");
//				fields_list.add(status);
//				LmMerchOrderService.updateByFields(fields_list, refund.getOrderid()+"");
				
				if(lmOrder.getStatus().equals("4")) {
					//退回账户
					LmMerchInfo _lmMerchInfo = lmMerchInfoService.findById(refund.getMerchid()+"");
					_lmMerchInfo.setCredit(lmMerchInfo.getCredit().subtract(refund.getRefundmoney()));
					lmMerchInfoService.updateService(_lmMerchInfo);
				}
				
				lmOrder.setStatus("-1");//交易失败
				lmOrder.setBackstatus(2);//已退款
				lmOrderService.updateService(lmOrder);
				refund.setStatus(2);
				refund.setFinishtime(new Date());
				LmMerchOrderService.updRefundLog(refund);
				
			
				
				BigDecimal money=order.getRealpayprice();
				//返余额给客户
//				LmMember member=lmMemberService.findById(order.getMemberid()+"");
//				BigDecimal before_credit2=member.getCredit2();
//				BigDecimal after_credit2=before_credit2.add(money);
//				member.setCredit2(after_credit2);
//				lmMemberService.updateService(member);
				
			//	Map<String,Object> result=this.refund(refund.getOrderid()+"", money.toString());
				
				//插入积分余额记录
				LmMemberLog log2=new LmMemberLog();
				log2.setCreatetime(new Date());
				log2.setNum(money);
				log2.setMark("订单退款:"+money);
				log2.setMemberid(order.getMemberid());
				log2.setType(2);
				lmMemberService.addMemberLog(log2);
				LmOrderLog lmOrderLog = new LmOrderLog();
				lmOrderLog.setOperate("订单退款:"+money);
				lmOrderLog.setOperatedate(new Date());
				lmOrderLog.setOrderid(refund.getOrderid()+"");
				lmOrderLogService.insert(lmOrderLog);
				HttpClient httpClient = new HttpClient();
				httpClient.send("交易消息",order.getMemberid()+"","退款请求已完成，请确认是否收到退款");
//				pushmsgService.send(lmMerchInfo.getMember_id(),"退款请求已完成，请确认是否收到退款","2",order.getMemberid(),1);
			}else {
				jsonResult.setCode(JsonResult.ERROR);
				jsonResult.setMsg("type错误");
				return jsonResult;
			}

			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}
	
	
	 
	
	/**
	 * 查看物流
	 * @param request
	 * @return
	 */
	@RequestMapping("get_express_info")
	@ResponseBody
	public JsonResult getExpressInfo(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("code"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("code为空");
			return jsonResult;
		}	
		if (StringUtils.isEmpty(request.getParameter("sn"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("sn为空");
			return jsonResult;
		}	
		try {
			ExpressUtil expressUtil=new ExpressUtil();
			String res=	expressUtil.synQueryData(request.getParameter("code"), request.getParameter("sn"), "", "", "", 0);
			
			jsonResult.setData(res);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
		}
		
		return  jsonResult;
	}
	
	

}
