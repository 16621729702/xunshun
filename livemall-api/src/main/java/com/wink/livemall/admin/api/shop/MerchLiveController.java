package com.wink.livemall.admin.api.shop;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wink.livemall.admin.util.*;
import com.wink.livemall.admin.util.httpclient.HttpClient;
import com.wink.livemall.goods.dto.LivedGood;
import com.wink.livemall.live.dto.*;
import com.wink.livemall.live.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wink.livemall.admin.api.good.GoodController;
import com.wink.livemall.goods.service.GoodService;
import com.wink.livemall.goods.service.MerchGoodService;
import com.wink.livemall.merch.dto.LmMerchInfo;
import com.wink.livemall.merch.service.LmMerchInfoService;

@Controller
@RequestMapping("merchlive")
public class MerchLiveController {

	Logger logger = LogManager.getLogger(GoodController.class);

	@Autowired
	private MerchGoodService merchGoodService;

	@Autowired
	private GoodService goodService;

	@Autowired
	private LmLiveGoodService lmLiveGoodService;

	@Autowired
	private LmLiveCategoryService lmLiveCategoryService;

	@Autowired
	private LmMerchInfoService lmMerchInfoService;

	@Autowired
	private LmMerchLiveService LmMerchLiveService;
	
	@Autowired
	private LmLiveLogService lmLiveLogService;

	@Autowired
	private LmLiveInfoService lmLiveInfoService;

	/**
	 * 直播商品列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("get_live_goods_list_merch")
	@ResponseBody
	public JsonResult get_livegoods_list_merch(HttpServletRequest request,
											   @RequestParam(required = true) int liveid,
											   @RequestParam(required = false,defaultValue = "10") int pagesize,
											   @RequestParam(required = false,defaultValue = "1") int pageindex) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("liveid"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("liveid为空");
			return jsonResult;
		}
		try {
			List<Map<String, Object>> list=lmLiveGoodService.findLiveGoodByLiveid(liveid);
			jsonResult.setCode(jsonResult.SUCCESS);
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("pageindex", pageindex);
			res.put("pagesize",pagesize);
			res.put("list", PageUtil.startPage(list,pageindex,pagesize));
			jsonResult.setData(res);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 删除直播商品
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("del_live_goods")
	@ResponseBody
	public JsonResult auditOrder(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id为空");
			return jsonResult;
		}
		try {
			lmLiveGoodService.delGood(Integer.parseInt(request.getParameter("id")));
			jsonResult.setCode(jsonResult.SUCCESS);
		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

	/**
	 * 添加商品
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("add_live_goods")
	@ResponseBody
	public JsonResult addLiveGoods(HttpServletRequest request,
								   @RequestParam(required = true) String good_id,
								   @RequestParam(required = true) String liveid) {
		JsonResult jsonResult = new JsonResult();
		try {
			LmLiveGood lmLiveGood = lmLiveGoodService.checkGood(good_id,liveid);
			if (lmLiveGood!=null) {
				jsonResult.setMsg("已添加过");
				jsonResult.setCode(JsonResult.ERROR);
				return jsonResult;
			}
			LmLiveGood good  = new LmLiveGood();
			good.setLiveid(Integer.parseInt(liveid));
			good.setGood_id(Integer.parseInt(good_id));
			lmLiveGoodService.addGood(good);
			Map<String, Object> m=new HashMap<String, Object>();
			m.put("goods", good);
			jsonResult.setData(m);
			jsonResult.setCode(jsonResult.SUCCESS);

		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}

		return jsonResult;
	}

	

	/**
	 * 开关直播间
	 * 
	 * @return type 1 开启 2关闭
	 */
	@RequestMapping("upd_live_status")
	@ResponseBody
	public JsonResult updLiveStatus(HttpServletRequest request) {
		HttpClient client = new HttpClient();
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(request.getParameter("id"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id为空");
			return jsonResult;
		}
		if (StringUtils.isEmpty(request.getParameter("type"))) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("type为空");
			return jsonResult;
		}
		LmLive live = LmMerchLiveService.findLiveByMerchid(Integer.parseInt(request.getParameter("id")));
		if (live == null) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("没找到直播间");
			return jsonResult;
		}
/*		if(live.getIsstart()==1){
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("该直播间正在被使用");
			return jsonResult;
		}*/
		if (request.getParameter("type").equals("1")) {
			//创建直播间群组
			Map map = client.creatgroup(live.getId()+"");
			live.setLivegroupid(map.get("GroupId")+"");
			live.setIsstart(1);
			LmMerchLiveService.updLive(live);
			LmLiveLog liveLog=new LmLiveLog();
			liveLog.setMerchid(Integer.parseInt(request.getParameter("id")));
			liveLog.setStarttime(new Date());
			liveLog.setStatus(1);
			liveLog.setLiveid(live.getId());
			lmLiveLogService.addLog(liveLog);
		} else {
			//清除观看人数
			LmLiveInfo lmLiveInfo =lmLiveInfoService.findLiveInfo(live.getId());
			lmLiveInfo.setAddnum(0);
			lmLiveInfoService.updateService(lmLiveInfo);
			//清除直播间商品
			List<LivedGood> oldgood = goodService.findLivedGoodByLiveid(live.getId());
			for(LivedGood good:oldgood){
					good.setStatus(-1);
					goodService.updateLivedGood(good);
			}
			//注销群组
			client.deleteGroup(live.getLivegroupid());
			live.setIsstart(0);
			live.setWatchnum(0);
			live.setLivegroupid("");
			LmMerchLiveService.updLive(live);
			LmLiveLog liveLog=lmLiveLogService.findLastLog(request.getParameter("id"));
			if(liveLog!=null) {
				int diff=0;
				Date end=new Date();
				Date start=liveLog.getStarttime();
				diff=(int)(end.getTime()/1000-start.getTime()/1000);
				liveLog.setEndtime(end);
				liveLog.setDiff(diff);
				liveLog.setStatus(2);
				lmLiveLogService.upd(liveLog);
			}
		}
		return jsonResult;
	}

	
	public static void main(String[] args) {
		Date d=new Date();
		System.out.println(d.getTime());
	}
	
	/**
	 * 检查商户直播间状态 status 1未申请 2待审核 3正常 4禁用
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("check_room")
	@ResponseBody
	public JsonResult checkRoom(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		// 判断商户是否存在可用
		String merchid = request.getParameter("id");
		if (merchid == null) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("id为空");
			return jsonResult;
		}
		int fg = lmMerchInfoService.checkMerchEnable(merchid);
		if (fg == 0) {
			jsonResult.setCode(jsonResult.ERROR);
			jsonResult.setMsg("商户不存在或者被禁用");
			return jsonResult;
		}
		LmLive live = LmMerchLiveService.findLiveByMerchid(Integer.parseInt(request.getParameter("id")));
		Map<String, Object> res = new HashMap<String, Object>();
		if (live == null) {
			jsonResult.setCode(JsonResult.SUCCESS);
			res.put("status", 1);
			jsonResult.setData(res);

		} else {
			if (live.getStatus() == -1) {
				jsonResult.setCode(JsonResult.SUCCESS);
				res.put("status", 2);

			} else if (live.getStatus() == 0) {
				jsonResult.setCode(JsonResult.SUCCESS);
				res.put("status", 3);
				res.put("liveid", live.getId());

			} else if (live.getStatus() == 1) {
				jsonResult.setCode(JsonResult.SUCCESS);
				res.put("status", 4);

			}
			jsonResult.setData(res);
		}

		return jsonResult;
	}

	
	
	/**
	 * 获取直播分类 返回二级分类集合
	 */
	@RequestMapping("get_live_cate")
	@ResponseBody
	public JsonResult findGoodsWarehouse() {
		JsonResult jsonResult = new JsonResult();

		try {
			List<LmLiveCategory> list = lmLiveCategoryService.findActiveList();
			List<Menu> menulist = new ArrayList<Menu>();
			if (list.size() > 0) {
				int len = list.size();
				for (int i = 0; i < len; i++) {
					LmLiveCategory cate = list.get(i);
					if (cate.getPid() == 0) {
						List<Menu> child = getChild(list, cate.getId());
						Menu menu = new Menu();
						menu.setId(list.get(i).getId());
						menu.setName(list.get(i).getName());
						menu.setChildren(child);
						menulist.add(menu);
					}
				}

			}
			Map<String, Object> res = new HashMap<String, Object>();
			res.put("list", menulist);
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
	 * 筛选子分类
	 * 
	 * @param list
	 * @param parentid
	 * @return
	 */
	private List<Menu> getChild(List<LmLiveCategory> list, int parentid) {
		List<Menu> child = new ArrayList<Menu>();
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getPid() == parentid) {
				Menu menu = new Menu();
				menu.setId(list.get(i).getId());
				menu.setName(list.get(i).getName());
				menu.setPid(parentid);
				child.add(menu);
			}
		}

		return child;
	}

	/**
	 * 申请直播间
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("add_room_apply")
	@ResponseBody
	public JsonResult roomApply(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		VerifyFields ver = new VerifyFields();
		try {
			String[] fields = { "merch_id", "name", "categoryid", "img" };
			Map<String, Object> res = ver.verifytoEntity(fields, request, "com.wink.livemall.live.dto.LmLive");
			
			if ((int) res.get("error") == 0) {
				LmMerchInfo info=lmMerchInfoService.findById(request.getParameter("merch_id"));
				if(info.getIslive()==0) {
					jsonResult.setCode(JsonResult.ERROR);
					jsonResult.setMsg("没有权限");
					return jsonResult;
				}
				LmLive live = LmMerchLiveService.findLiveByMerchid(Integer.parseInt(request.getParameter("merch_id")));
				System.out.println(live);
				if (live != null) {
					jsonResult.setCode(JsonResult.ERROR);
					jsonResult.setMsg("已申请");
					return jsonResult;
				}
				LmLive entity = (LmLive) res.get("entity");
				entity.setStatus(-1);
				if(info.getIschipped()==1){
					entity.setType(1);
				}else{
					entity.setType(0);
				}
				LmMerchLiveService.addLiveApply(entity);
				LmLive lives = LmMerchLiveService.findLiveByMerchid(Integer.parseInt(request.getParameter("merch_id")));
				LmLiveInfo lmLiveInfo=new LmLiveInfo();
				lmLiveInfo.setLive_id(lives.getId());
				lmLiveInfo.setCreate_time(new Date());
				lmLiveInfoService.insertService(lmLiveInfo);
				jsonResult.setCode(JsonResult.SUCCESS);
			} else {
				jsonResult.setMsg((String) res.get("msg"));
				jsonResult.setCode(JsonResult.ERROR);
				return jsonResult;
			}

		} catch (Exception e) {
			jsonResult.setMsg(e.getMessage());
			jsonResult.setCode(JsonResult.ERROR);
			logger.error(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * @return 构造菜单返回对象
	 */
	class Menu {
		private int id;
		private int pid;
		private String name;
		private List<Menu> children;

		public int getPid() {
			return pid;
		}

		public void setPid(int pid) {
			this.pid = pid;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Menu> getChildren() {
			return children;
		}

		public void setChildren(List<Menu> children) {
			this.children = children;
		}

	}

}
