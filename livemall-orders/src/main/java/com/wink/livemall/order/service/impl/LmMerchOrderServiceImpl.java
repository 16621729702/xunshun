package com.wink.livemall.order.service.impl;

import com.wink.livemall.order.dao.LmMerchOrderDao;
import com.wink.livemall.order.dao.LmOrderRefundLogDao;
import com.wink.livemall.order.dao.LmShopOrderDao;
import com.wink.livemall.order.dto.LmOrder;
import com.wink.livemall.order.dto.LmOrderRefundLog;
import com.wink.livemall.order.service.LmMerchOrderService;
import com.wink.livemall.order.service.LmOrderService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LmMerchOrderServiceImpl implements LmMerchOrderService {

    @Resource
    private LmMerchOrderDao LmMerchOrderDao;
    
    @Resource
    private LmOrderRefundLogDao LmOrderRefundLogDao;

	@Override
	public List<Map<String, Object>> findListByCondient(Map<String, String> condient) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=LmMerchOrderDao.findListByCondition(condient);
		
		
		return list;
	}

	@Override
	public List<Map<String, Object>> findPage(Map<String, String> parmas) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.getpage(parmas);
	}

	@Override
	public void updateByFields(List<Map<String, String>> params, String id) {
		// TODO Auto-generated method stub
		LmMerchOrderDao.updateByFields(params,id);
	}

	@Override
	public boolean checkExist(String id) {
		// TODO Auto-generated method stub
		
		return LmMerchOrderDao.findByOrderid(id)==null?false:true;
	}

	@Override
	public void addRefundLog(LmOrderRefundLog entity) {
		// TODO Auto-generated method stub
		LmOrderRefundLogDao.insertSelective(entity);
	}

	@Override
	public Map<String, Object> staticOrderOk(String merchid) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.staticOrderOk(merchid);
	}

	@Override
	public Map<String, Object> staticOrderPay(String merchid) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.staticOrderPay(merchid);
	}

	@Override
	public Map<String, Object> staticOrderRefund(String merchid,int backStatus) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.staticOrderRefund(merchid, backStatus);
	}

	@Override
	public Map<String, Object> staticOrderEarn(String merchid,int status) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.staticOrderEarn(merchid,status);
	}

	@Override
	public List<Map<String, Object>> orderOkList(Map<String, String> parmas) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.orderOkList(parmas);
	}

	@Override
	public List<Map<String, Object>> orderPayList(Map<String, String> parmas) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.orderPayList(parmas);
	}

	@Override
	public List<Map<String, Object>> orderRefundList(Map<String, String> parmas) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.orderRefundList(parmas);
	}

	@Override
	public List<Map<String, Object>> orderEarnList(Map<String, String> parmas) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.orderEarnList(parmas);
	}

	@Override
	public Map<String, Object> staticLive(String merchid) {
		return LmMerchOrderDao.staticLive(merchid);
	}

	@Override
	public int staticLiveNum(String merchid) {
		return LmMerchOrderDao.staticLiveNum(merchid);
	}

	@Override
	public List<Map<String, Object>> orderLiveList(Map<String, String> parmas) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.orderLiveList(parmas);
	}

	@Override
	public LmOrderRefundLog getRefundLog(int id) {
		// TODO Auto-generated method stub
		return LmOrderRefundLogDao.selectByPrimaryKey(id);
	}

	@Override
	public void updRefundLog(LmOrderRefundLog log) {
		// TODO Auto-generated method stub
		LmOrderRefundLogDao.updateByPrimaryKeySelective(log);
	}

	@Override
	public List<Map<String, Object>> findChildrenOrder(Map<String, String> params) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.findChildrenOrder(params);
	}

	@Override
	public Map<String, Object> findChippedByGoodsId(String goodsid) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.findChippedByGoodsId(goodsid);
	}

	@Override
	public List<Map<String, Object>> findChippedOrder(String porderid,String order) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.findChippedOrder(porderid,order);
	}

	@Override
	public List<LmOrder> findChippedOrder2(int porderid) {
		return LmMerchOrderDao.findChippedOrder2(porderid);
	}


	@Override
	public Map<String, Object> staticAll(String merchid) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.staticAll(merchid);
	}

	@Override
	public Map<String, Object> staticOrderMonth(String merchid,String type) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.staticOrderMonth(merchid, type);
	}

	@Override
	public int countPay(String merchid) {
		// TODO Auto-generated method stub
		
		return LmMerchOrderDao.countPay(merchid);
	}

	@Override
	public int countRefund(String merchid) {
		// TODO Auto-generated method stub
		return LmMerchOrderDao.countRefund(merchid);
	}



	@Override
	public LmOrderRefundLog getRefundLogByorderid(String id) {
		// TODO Auto-generated method stub
		return LmOrderRefundLogDao.findByorderid(id);
	}


	@Override
	public Map<String, Object> orderLogList(String merId, Integer type, String startTime, String entTime) throws ParseException {
		List<Map<String, Object>> map=new LinkedList<>();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTimes = dateFormat.parse(startTime);
		Date entTimes = dateFormat.parse(entTime);
		BigDecimal max =new BigDecimal(0);
		for(int i =1;i<100;i++){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startTimes);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + i-1);
			Date startTimeNew = calendar.getTime();
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(java.util.Calendar.DATE);
			calendar.setTime(startTimes);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + i);
			Date entTimeNew = calendar.getTime();
			if(startTimeNew.getTime()>=entTimes.getTime()){
				break;
			}
			Map<String, Object> orderLogList = LmMerchOrderDao.orderLogList(merId, type, dateFormat.format(startTimeNew),  dateFormat.format(entTimeNew));
			BigDecimal price=(BigDecimal)orderLogList.get("price");
			if(price.compareTo(max)>-1){
				max=price;
			}
			String dateName=month+"月"+day+"日";
			orderLogList.put("dateName",dateName);
			map.add(orderLogList);
		}
		Map<String, Object> orderLogList = LmMerchOrderDao.orderLogList(merId, type, startTime, entTime);
		orderLogList.put("dateName","合计");
		Map<String, Object> mapList=new HashMap<>();
		mapList.put("list",map);
		mapList.put("total",orderLogList);
		mapList.put("max",max);
		return mapList;
	}

	@Override
	public Map<String, Object>  staticLiveList(String merId, String startTime, String entTime) throws ParseException {
		List<Map<String, Object>> map=new LinkedList<>();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTimes = dateFormat.parse(startTime);
		Date entTimes = dateFormat.parse(entTime);
		BigDecimal max =new BigDecimal(0);
		for(int i =1;i<100;i++){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startTimes);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + i-1);
			Date startTimeNew = calendar.getTime();
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(java.util.Calendar.DATE);
			calendar.setTime(startTimes);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + i);
			Date entTimeNew = calendar.getTime();
			if(startTimeNew.getTime()>=entTimes.getTime()){
				break;
			}
			Map<String, Object> orderLogList = LmMerchOrderDao.staticLiveList(merId, dateFormat.format(startTimeNew),  dateFormat.format(entTimeNew));
			BigDecimal price=(BigDecimal)orderLogList.get("price");
			if(price.compareTo(max)>-1){
				max=price;
			}
			String dateName=month+"月"+day+"日";
			orderLogList.put("dateName",dateName);
			map.add(orderLogList);
		}
		Map<String, Object> orderLogList = LmMerchOrderDao.staticLiveList(merId,startTime, entTime);
		orderLogList.put("dateName","合计");
		Map<String, Object> mapList=new HashMap<>();
		mapList.put("list",map);
		mapList.put("total",orderLogList);
		mapList.put("max",max);
		return mapList;
	}

}
