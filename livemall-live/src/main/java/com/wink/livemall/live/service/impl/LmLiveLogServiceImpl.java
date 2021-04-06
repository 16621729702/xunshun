package com.wink.livemall.live.service.impl;

import com.wink.livemall.live.dao.LmLiveLogDao;
import com.wink.livemall.live.dto.LmLiveLog;
import com.wink.livemall.live.service.LmLiveLogService;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.wink.livemall.order.dao.LmMerchOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LmLiveLogServiceImpl implements LmLiveLogService {

	@Autowired
	private LmLiveLogDao lmLiveLogDao;
	@Autowired
	private LmMerchOrderDao lmMerchOrderDao;

	@Override
	public List<LmLiveLog> findPage(Map<String, String> params) {
		// TODO Auto-generated method stub
		return lmLiveLogDao.findPage(params);
	}

	@Override
	public void addLog(LmLiveLog liveLog) {
		// TODO Auto-generated method stub
		lmLiveLogDao.insertSelective(liveLog);
	}

	@Override
	public LmLiveLog findLastLog(String merchid) {
		// TODO Auto-generated method stub
		return lmLiveLogDao.findLastLog(merchid);
	}

	@Override
	public void upd(LmLiveLog liveLog) {
		// TODO Auto-generated method stub
		lmLiveLogDao.updateByPrimaryKeySelective(liveLog);
	}

	@Override
	public Long countTime(String merchid) {
		// TODO Auto-generated method stub
		return lmLiveLogDao.countTime(merchid);
	}



	@Override
	public Map<String, Object> countTimeList(String merId, String startTime, String entTime) throws ParseException {
		List<Map<String, Object>> map=new LinkedList<>();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTimes = dateFormat.parse(startTime);
		Date entTimes = dateFormat.parse(entTime);
		Long max =0L;
		for(int i =1;i<100;i++){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startTimes);
			Map<String, Object> live =new HashMap<>();
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
			Long aLong = lmLiveLogDao.countTimeList(merId,  dateFormat.format(startTimeNew),  dateFormat.format(entTimeNew));
			if(max.longValue()<aLong.longValue()){
				max=aLong;
			}
			String dateName=month+"月"+day+"日";
			live.put("dateName",dateName);
			live.put("times",aLong);
			map.add(live);
		}
		Long aLong = lmLiveLogDao.countTimeList(merId, startTime, entTime);
		Map<String, Object> count =new HashMap<>();
		count.put("dateName","合计");
		count.put("times",getTimeZHCNBySeconds(aLong));
		Map<String, Object> mapList=new HashMap<>();
		mapList.put("list",map);
		mapList.put("total",count);
		mapList.put("max",max);
		return mapList ;
	}

	@Override
	public  Map<String, Object> liveNumList(String merId, String startTime, String entTime) throws ParseException {
		List<Map<String, Object>> map=new LinkedList<>();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTimes = dateFormat.parse(startTime);
		Date entTimes = dateFormat.parse(entTime);
		int liveNumMax =0;
		for(int i =1;i<1000;i++){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startTimes);
			Map<String, Object> live =new HashMap<>();
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
			List<LmLiveLog> LmLiveLog = lmLiveLogDao.liveLogNum(merId,  dateFormat.format(startTimeNew),  dateFormat.format(entTimeNew));
			int size = LmLiveLog.size();
			if(liveNumMax<size){
				liveNumMax=size;
			}
			String dateName=month+"月"+day+"日";
			live.put("startTime",dateFormat.format(startTimeNew));
			calendar.setTime(entTimeNew);
			calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - 1);
			Date time = calendar.getTime();
			live.put("endTime",dateFormat.format(time));
			live.put("dateName",dateName);
			live.put("liveNum",LmLiveLog.size());

			map.add(live);
		}
		List<LmLiveLog> LmLiveLog = lmLiveLogDao.liveLogNum(merId,  dateFormat.format(startTimes),  dateFormat.format(entTimes));
		Map<String, Object> mapList=new HashMap<>();
		mapList.put("dateName","合计");
		mapList.put("total",LmLiveLog.size());
		mapList.put("startTime",dateFormat.format(startTimes));
		mapList.put("endTime",dateFormat.format(entTimes));
		mapList.put("liveNumMax",liveNumMax);
		Map<String, Object> List=new HashMap<>();
		List.put("LiveList",map);
		List.put("Total",mapList);
		return List;
	}

	@Override
	public Map<String, Object> dayLiveNumList(String merId, String startTime, String entTime)throws ParseException  {
		List<Map<String, Object>> map=new LinkedList<>();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTimes = dateFormat.parse(startTime);
		Date entTimes = dateFormat.parse(entTime);
		List<LmLiveLog> lmLiveLog = lmLiveLogDao.liveLogNum(merId,  dateFormat.format(startTimes),  dateFormat.format(entTimes));
		int diffMax =0;
		int concurrentMax =0;
		BigDecimal priceMax =new BigDecimal(0);
		if(lmLiveLog.size()>0){
			for(LmLiveLog log:lmLiveLog){
				if(concurrentMax<log.getConcurrent()){
					concurrentMax=log.getConcurrent();
				}
				if(diffMax<log.getDiff()){
					diffMax=log.getDiff();
				}
				Map<String, Object> live =new HashMap<>();
				live.put("create_time",dateFormat.format(log.getStarttime()));
				live.put("diff",log.getDiff());
				live.put("concurrent",log.getConcurrent());
				Map<String, Object> stringObjectMap = lmMerchOrderDao.orderLogList(merId, 4, dateFormat.format(log.getStarttime()), dateFormat.format(log.getEndtime()));
				BigDecimal price =(BigDecimal)stringObjectMap.get("price");
				if(price.compareTo(priceMax)>0){
					priceMax=price;
				}
				live.putAll(stringObjectMap);
				map.add(live);
			}
		}
		Map<String, Object> stringObjectMap = lmMerchOrderDao.orderLogList(merId, 4, dateFormat.format(startTimes), dateFormat.format(entTimes));
		Map<String, Object> Total= lmLiveLogDao.liveLogTotal(merId,  dateFormat.format(startTimes),  dateFormat.format(entTimes));
		BigDecimal priceTotal =(BigDecimal)stringObjectMap.get("price");
		Map<String, Object> mapList=new HashMap<>();
		mapList.put("dateName","合计");
		mapList.put("diffMax",diffMax);
		mapList.put("concurrentMax",concurrentMax);
		mapList.put("priceMax",priceMax);
		mapList.put("priceTotal",priceTotal);
		mapList.putAll(Total);
		Map<String, Object> List=new HashMap<>();
		List.put("LiveList",map);
		List.put("Total",mapList);
		return List;
	}

	public static String getTimeZHCNBySeconds(Long seconds) {
		String result = "0";
		if (seconds != null && seconds.compareTo(0L) > 0) {
			try {
				long d = seconds / 86400;
				long h = (seconds %= 86400) / 3600;
				long m = (seconds %= 3600) / 60;
				long s = seconds % 60;

				if (d > 0) {
					result = d + "天" + h + "小时" + m + "分" + s + "秒";
				} else if (h > 0) {
					result = h + "小时" + m + "分" + s + "秒";
				} else if (m > 0) {
					result = m + "分" + s + "秒";
				} else {
					result = s + "秒";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
