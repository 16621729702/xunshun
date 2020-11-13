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

import com.google.gson.Gson;
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
import com.wink.livemall.merch.dto.LmMerchConfigs;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchConfigsService;
import com.wink.livemall.order.dto.LmExpress;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderRefundLog;
import com.wink.livemall.order.service.LmMerchOrderService;
import com.wink.livemall.order.service.LmOrderExpressService;
import com.wink.livemall.order.service.LmOrderService;
import com.wink.livemall.sys.msg.service.PushmsgService;
import com.wink.livemall.sys.setting.dto.Configs;
import com.wink.livemall.sys.setting.service.ConfigsService;
import com.wink.livemall.video.dto.LmVideoCategoary;
import com.wink.livemall.video.dto.LmVideoCore;
import com.wink.livemall.video.service.LmVideoCategoaryService;
import com.wink.livemall.video.service.LmVideoCoreService;

@Controller
@RequestMapping("merchmsg")
public class MerchMsgController {

	Logger logger = LogManager.getLogger(GoodController.class);

	@Autowired
	private PushmsgService pushmsgService;

	@Autowired
	private LmMerchConfigsService lmMerchConfigsService;
	
	
	@Autowired
	private ConfigsService configsService;
	
	/**
	 * 消息列表
	 * @return
	 */
	@RequestMapping("get_msg_list")
	@ResponseBody
	public JsonResult findPage(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		String logo=findConfigByField("logo");
		
		if (StringUtils.isEmpty(request.getParameter("memberid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("memberid为空");
			return jsonResult;
		}
		try {
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String, String> params = new HashMap<>();
			params.put("pagesize", request.getParameter("pagesize"));
			params.put("pageindex", request.getParameter("pageindex"));
			params.put("memberid", request.getParameter("memberid"));
			List<Map<String, Object>> list =pushmsgService.findPageMerch(params);
			List<Map<String, Object>> list2=new ArrayList<Map<String,Object>>();
			if(list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> m=list.get(i);
					Map<String, Object> m2=new HashMap<String, Object>();
					if((int)m.get("type")==1) {
						m2.put("img",logo);
						m2.put("name","官方");
					}else {
						m2.put("img",StringUtils.isEmpty(m.get("avatar"))?"":m.get("avatar"));
						m2.put("name",StringUtils.isEmpty(m.get("nickname"))?"":m.get("nickname"));
					}
					m2.put("type",m.get("type"));
					m2.put("isread",m.get("isread"));
					m2.put("time",sf.format(m.get("createtime")));
					m2.put("msg",m.get("content"));
					m2.put("sendid",m.get("sendid"));
					m2.put("recevieid",m.get("recevieid"));
					list2.add(m2);
				}
			}
			jsonResult.setCode(jsonResult.SUCCESS);
			jsonResult.setData(list2);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

	
	/**
	 * 根据配置参数获取配置内容
	 * @param field
	 * @return
	 */
	private String findConfigByField(String field) {
		Configs configs=configsService.findByTypeId(1);
		String json=configs.getConfig();
		Gson gson=new Gson();
		Map<String, String> jsonmap=gson.fromJson(json, Map.class);
		
		return jsonmap.get(field);
		
	}
	
	
	
}
