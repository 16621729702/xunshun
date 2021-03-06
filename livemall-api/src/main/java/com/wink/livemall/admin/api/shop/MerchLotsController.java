package com.wink.livemall.admin.api.shop;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.goods.dto.*;
import com.wink.livemall.live.dto.LmLive;
import com.wink.livemall.live.service.LmLiveService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MapAccessor;
import org.springframework.data.redis.connection.ReactiveSetCommands.SRemCommand;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.VerifyFields;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.LotsService;
import com.wink.livemall.goods.service.MerchGoodService;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberLog;
import com.wink.livemall.member.service.LmMemberService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.order.dto.LmExpress;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderGoods;
import com.wink.livemall.order.dto.LmOrderRefundLog;
import com.wink.livemall.order.service.LmMerchOrderService;
import com.wink.livemall.order.service.LmOrderExpressService;
import com.wink.livemall.order.service.LmOrderGoodsService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.video.dto.LmVideoCategoary;
import com.wink.livemall.video.dto.LmVideoCore;
import com.wink.livemall.video.service.LmVideoCategoaryService;
import com.wink.livemall.video.service.LmVideoCoreService;

@Controller
@RequestMapping("merchlots")
public class MerchLotsController {

	Logger logger = LogManager.getLogger(GoodController.class);

	@Autowired
	private LotsService lotService;

	@Autowired
	private LmMerchOrderService lmMerchOrderService;

	@Autowired
	private LmOrderGoodsService lmOrderGoodsService;

	@Autowired
	private LmOrderService lmOrderService;

	@Autowired
	private LmLiveService lmLiveService;

	@Autowired
	private GoodService goodService;
	/**
	 * ?????????????????? ?????????
	 * ????????????????????????????????????  ???????????????????????? ????????????????????????????????????
	 * ??????????????????????????? ???????????? ???????????????
	 * @param request
	 * @return
	 */
	@RequestMapping("draw_lots")
	@ResponseBody
	@Transactional
	public JsonResult drawLots(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();

		try {
			String[] fields = { "imgs", "goodsid","merchid"};
			VerifyFields ver = new VerifyFields();
			Map<String, Object> res = ver.verifytoEntity(fields, request, "com.wink.livemall.goods.dto.LmLotsInfo");
			if ((int) res.get("error") == 0) {
				LmLotsInfo entity = (LmLotsInfo) res.get("entity");
				LmOrder porder =lmOrderService.findTopOrder(entity.getGoodsid(),entity.getMerchid());
				LmShareGood oldgood = goodService.findshareById(entity.getGoodsid());
				if(porder==null) {
					jsonResult.setMsg("?????????????????????");
					jsonResult.setCode(JsonResult.ERROR);
					return jsonResult;
				}
				if(porder.getLots_log_id()>0) {
					jsonResult.setMsg("?????????");
					jsonResult.setCode(JsonResult.ERROR);
					return jsonResult;
				}
				List<Map<String, Object>> orders=lmMerchOrderService.findChippedOrder(porder.getId()+"","1");
				if(orders.size()<oldgood.getChipped_num()) {
					jsonResult.setMsg("????????????");
					jsonResult.setCode(JsonResult.ERROR);
					return jsonResult;
				}
				if(orders.size()>oldgood.getChipped_num()) {
					jsonResult.setMsg("????????????");
					jsonResult.setCode(JsonResult.ERROR);
					return jsonResult;
				}
				LmLotsInfo info=(LmLotsInfo) res.get("entity");
				LmLive lmLive = lmLiveService.findByMerchid(info.getMerchid());
				if(!StringUtils.isEmpty(request.getParameter("videos"))) {
					info.setVideos(request.getParameter("videos"));
				}
				if(lmLive!=null){
					info.setLiveid(lmLive.getId());
				}
				info.setCreatetime(new Date());
				info.setType(1);
				lotService.addInfo(info);
				int lots_info_id=info.getId();
				//??????????????? ????????????lots_info_id
				List<Map<String, String>> fields_list =new ArrayList<Map<String,String>>();
				Map<String, String> lots_info_id_ = new HashMap<String, String>();
				lots_info_id_.put("field", "lots_info_id");
				lots_info_id_.put("value", lots_info_id+"");
				fields_list.add(lots_info_id_);
				lots_info_id_.put("field", "lots_status");
				lots_info_id_.put("value", "1");
				fields_list.add(lots_info_id_);
				lmMerchOrderService.updateByFields(fields_list, porder.getId()+"");
				int totalnum=oldgood.getChipped_num();
				List<Integer> rlist=new ArrayList<Integer>();
				while(rlist.size()<totalnum) {
					int r=(int)(Math.random()*totalnum);
					int fg=0;
					for (int i = 0; i < rlist.size(); i++) {
						if(rlist.get(i)==r) {
							fg=1;
							break;
						}
					}
					if(fg==0) {
						rlist.add(r);
					}
				}
				List<Map<String, Object>> lots=new ArrayList<Map<String,Object>>();
				for (int i = 0; i < rlist.size(); i++) {
					int index=rlist.get(i);
					Map<String, Object> m=orders.get(index);
					Map<String, Object> lot=new HashMap<String, Object>();
					LmLotsLog lmLotsLog=new LmLotsLog();
					lmLotsLog.setCreatetime(new Date());
					lmLotsLog.setGoodsid(Integer.parseInt(request.getParameter("goodsid")));
					lmLotsLog.setInfoid(lots_info_id);
					lmLotsLog.setLot_no(i+1);
					lmLotsLog.setMemberid((int)(m.get("memberid")));

					lotService.addLog(lmLotsLog);
					List<Map<String, String>> fields_list2 =new ArrayList<Map<String,String>>();
					Map<String, String> lots_log_id = new HashMap<String, String>();
					lots_log_id.put("field", "lots_log_id");
					lots_log_id.put("value", lmLotsLog.getId()+"");
					fields_list2.add(lots_log_id);
					Map<String, String> lots_log_info = new HashMap<String, String>();
					lots_log_info.put("field", "lots_log_no");
					lots_log_info.put("value", (i+1)+"");
					fields_list2.add(lots_log_info);
					Map<String, String> lots_info_id__ = new HashMap<String, String>();
					lots_info_id__.put("field", "lots_info_id");
					lots_info_id__.put("value", lots_info_id+"");
					fields_list2.add(lots_log_info);
					lmMerchOrderService.updateByFields(fields_list2, m.get("orderid").toString());

					lot.put("nickname",m.get("nickname"));
					lot.put("no",i+1);
					lots.add(lot);
				}
				logger.info("--------------????????????????????????---------------------");
				//??????????????????
				if(lmLive!=null){
					HttpClient httpClient = new HttpClient();
					httpClient.sendgroup(lmLive.getLivegroupid(),entity.getGoodsid()+"",3);
					logger.info("-----------------------------??????????????????id:"+entity.getGoodsid()+"-----------------------------------------");
				}
				jsonResult.setCode(JsonResult.SUCCESS);
				jsonResult.setData(lots);
			}else {
				jsonResult.setMsg((String) res.get("msg"));
				jsonResult.setCode(JsonResult.ERROR);
			}

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * ??????????????????
	 * @param request
	 * @return
	 */
	@RequestMapping("get_lots_result")
	@ResponseBody
	public JsonResult findLotsResult(HttpServletRequest request) {
		logger.info("-----------------------------------????????????????????????--------------------------------");
		JsonResult jsonResult=new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("goodsid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("goodsid??????");
			return jsonResult;
		}
		try {
			LmShareGood good = goodService.findshareById(Integer.parseInt(request.getParameter("goodsid")));
			Map<String, Object> porder=lmMerchOrderService.findChippedByGoodsId(request.getParameter("goodsid"));
			List<Map<String, Object>> orders=lmMerchOrderService.findChippedOrder(porder.get("orderid").toString(),"2");
			if(good!=null){
				for(Map<String, Object> map:orders){
					map.put("goodname",good.getName());
				}
			}
			jsonResult.setData(orders);
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return  jsonResult;
	}

	/**
	 * ??????
	 * @param request
	 * @return
	 */
	@RequestMapping("send_lots")
	@ResponseBody
	public JsonResult sendFLots(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("goodsid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("goodsid??????");
			return jsonResult;
		}
		try {
			Map<String, Object> porder=lmMerchOrderService.findChippedByGoodsId(request.getParameter("goodsid"));
			List<Map<String, String>> fields_list =new ArrayList<Map<String,String>>();
			Map<String, String> lots_status = new HashMap<String, String>();
			lots_status.put("field", "lots_status");
			lots_status.put("value", "1");
			fields_list.add(lots_status);
			lmMerchOrderService.updateByFields(fields_list, porder.get("orderid").toString());
			List<Map<String, Object>> orders=lmMerchOrderService.findChippedOrder(porder.get("orderid").toString(),"2");
			for (int i = 0; i < orders.size(); i++) {
				Map<String, Object> m=orders.get(i);
				List<Map<String, String>> fields_list_ =new ArrayList<Map<String,String>>();
				Map<String, String> lots_status_ = new HashMap<String, String>();
				lots_status_.put("field", "lots_status");
				lots_status_.put("value", "1");
				fields_list_.add(lots_status_);
				lmMerchOrderService.updateByFields(fields_list_, m.get("orderid").toString());
			}
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return  jsonResult;
	}


	/**
	 * ?????????????????????
	 * @param request
	 * @return
	 */
	@RequestMapping("get_main_order")
	@ResponseBody
	public JsonResult findLotsOrder(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("goodsid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("goodsid??????");
			return jsonResult;
		}
		try {
			Map<String, Object> porder=lmMerchOrderService.findChippedByGoodsId(request.getParameter("goodsid"));
			List<Map<String, Object>> orders=lmMerchOrderService.findChippedOrder(porder.get("orderid").toString(),"1");
			Map<String, Object> res = new HashMap<String, Object>();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			res.put("payed_num", orders.size());
			res.put("realpayprice", porder.get("realpayprice"));
			res.put("thumb", porder.get("thumb"));
			res.put("chipped_num", porder.get("chipped_num"));
			if(porder.get("chippedtime")!=null) {
				System.out.println(porder.get("chippedtime"));
				res.put("chipped_time", sf.format((Date)porder.get("chippedtime")));
			}
			if(orders.size()==(int)porder.get("chipped_num")) {
				res.put("chipped_status", 1);
			}else {
				res.put("chipped_status", 0);
			}

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
	 * ?????????????????????
	 * @param request
	 * @return
	 */
	@RequestMapping("get_chipped_order")
	@ResponseBody
	public JsonResult findChippedOrder(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("goodsid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("goodsid??????");
			return jsonResult;
		}
		try {
			Map<String, Object> porder=lmMerchOrderService.findChippedByGoodsId(request.getParameter("goodsid"));
			List<Map<String, Object>> orders=lmMerchOrderService.findChippedOrder(porder.get("orderid").toString(),"2");
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			if(orders.size()>0) {
				for (int i = 0; i < orders.size(); i++) {
					Map<String, Object> m=orders.get(i);
					Map<String, Object> res=new HashMap<String, Object>();
					res.put("lots_log_no", m.get("lots_log_no"));
					res.put("nickname",m.get("nickname") );
					res.put("mobile",m.get("mobile") );
					String str="";
					if((int)m.get("lots_status")==0) {
						str="?????????";
					}else if((int)m.get("lots_status")==1) {
						str="?????????";
					}

					res.put("lots_status",str);
					res.put("mobile",m.get("mobile") );
					list.add(res);
				}
			}
			jsonResult.setCode(jsonResult.SUCCESS);
			jsonResult.setData(list);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return  jsonResult;
	}

	/**
	 * ????????????????????????
	 * @param request
	 * @return
	 */
	@RequestMapping("add_lots_info")
	@ResponseBody
	public JsonResult addLotsInfo(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("goodsid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("goodsid??????");
			return jsonResult;
		}
		try {
			String[] fields = {  "goodsid","merchid","imgs","type"};
			VerifyFields ver = new VerifyFields();
			Map<String, Object> res = ver.verifytoEntity(fields, request, "com.wink.livemall.goods.dto.LmLotsInfo");
			if ((int) res.get("error") == 0) {
				LmLotsInfo info=(LmLotsInfo) res.get("entity");
				if(!StringUtils.isEmpty(request.getParameter("videos"))) {
					info.setVideos(request.getParameter("videos"));
				}
				info.setCreatetime(new Date());
				LmLotsInfo oldinfo = lotService.findLotsInfo2(info.getGoodsid(),info.getMerchid(),info.getType());
				if(oldinfo!=null){
					oldinfo.setVideos(info.getVideos());
					oldinfo.setImgs(info.getImgs());
					lotService.updateInfo(oldinfo);
				}else{
					lotService.addInfo(info);
				}
			}else {
				jsonResult.setMsg((String) res.get("msg"));
				jsonResult.setCode(JsonResult.ERROR);
			}

			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return  jsonResult;
	}

	/**
	 *????????????????????????
	 * @param request
	 * @return
	 */
	@RequestMapping("get_lots_info")
	@ResponseBody
	public JsonResult findLotsInfo(HttpServletRequest request) {
		JsonResult jsonResult=new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("goodsid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("goodsid??????");
			return jsonResult;
		}
		try {
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<Map<String, Object>> list=lotService.findLotsInfo(Integer.parseInt(request.getParameter("goodsid")));
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> m=list.get(i);
					m.put("timestr", sf.format((Date)m.get("createtime")));
				}
			}
			Map<String,Object> res=new HashMap<String, Object>();
			res.put("list", list);
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
