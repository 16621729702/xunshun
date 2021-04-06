package com.wink.livemall.admin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间工具类
 */
public class DateUtils {

	/**
	 * SimpleDateFormat yyyyMMddHHmmssSSS
	 */
	public static final SimpleDateFormat sdf_yMdHmsS = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	public static final SimpleDateFormat sdfyMdHm= new SimpleDateFormat("yyyyMMddHHmms");


	/**
	 * SimpleDateFormat yyyy-MM-dd HH:mm:ss
	 */
	public static final SimpleDateFormat sdf_yMdHms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * SimpleDateFormat yyyy-MM-dd HH:mm
	 */
	public static final SimpleDateFormat sdf_yMdHm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/**
	 * SimpleDateFormat yyyy-MM-dd
	 */
	public static final SimpleDateFormat sdf_yMd = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * SimpleDateFormat yyyy
	 */
	public static final SimpleDateFormat sdf_y = new SimpleDateFormat("yyyy");

	/**
	 * SimpleDateFormat yyyy-MM
	 */
	public static final SimpleDateFormat sdf_yM = new SimpleDateFormat("yyyy-MM");

	/**
	 * SimpleDateFormat MM-dd
	 */
	public static final SimpleDateFormat sdf_Md = new SimpleDateFormat("MM-dd");
	
	/**
	 * SimpleDateFormat HH:mm:ss
	 */
	public static final SimpleDateFormat sdf_Hms = new SimpleDateFormat("HH:mm:ss");

	/**
	 * SimpleDateFormat HH:mm
	 */
	public static final SimpleDateFormat sdf_Hm = new SimpleDateFormat("HH:mm");
	
	/**
	 * 
	 *SimpleDateFormat MM.dd
	 */
	public static SimpleDateFormat sdfMMdd = new SimpleDateFormat("MM.dd");
	/**
	 * 
	 *SimpleDateFormat MM月dd日
	 */
	public static SimpleDateFormat sdfMydr = new SimpleDateFormat("MM月dd日");
	/**
	 * SimpleDateFormat yyyy年MM月dd日
	 */
	public static SimpleDateFormat sdf_zsbn_yMd = new SimpleDateFormat("yyyy年MM月dd日");
	/**
	 * SimpleDateFormat yyyy年MM月dd日
	 */
	public static SimpleDateFormat sdf_dot_yMd = new SimpleDateFormat("yyyy.MM.dd");

	public static SimpleDateFormat sdf_zsbn_yM = new SimpleDateFormat("yyyy年MM月");

	public static String getWeekZHCNByDate(Date date) {
		String weekinfo = "";
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
				weekinfo = "星期日";
				break;
			case Calendar.MONDAY:
				weekinfo = "星期一";
				break;
			case Calendar.TUESDAY:
				weekinfo = "星期二";
				break;
			case Calendar.WEDNESDAY:
				weekinfo = "星期三";
				break;
			case Calendar.THURSDAY:
				weekinfo = "星期四";
				break;
			case Calendar.FRIDAY:
				weekinfo = "星期五";
				break;
			case Calendar.SATURDAY:
				weekinfo = "星期六";
				break;
			default:
				break;
			}
		}
		return weekinfo;
	}

	public static String getMonthEnglishByNum(Integer month) {
		String monthinfo = "";
		if (month != null) {
			switch (month) {
			case 1:
				monthinfo = "January";
				break;
			case 2:
				monthinfo = "February";
				break;
			case 3:
				monthinfo = "March";
				break;
			case 4:
				monthinfo = "April";
				break;
			case 5:
				monthinfo = "May";
				break;
			case 6:
				monthinfo = "June";
				break;
			case 7:
				monthinfo = "July";
				break;
			case 8:
				monthinfo = "August";
				break;
			case 9:
				monthinfo = "September";
				break;
			case 10:
				monthinfo = "October";
				break;
			case 11:
				monthinfo = "November";
				break;
			case 12:
				monthinfo = "December";
				break;
			default:
				break;
			}
		}
		return monthinfo;
	}

	public static String getWeekEnglishByDate(Date date) {
		String weekinfo = "";
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
				weekinfo = "Sunday";
				break;
			case Calendar.MONDAY:
				weekinfo = "Monday";
				break;
			case Calendar.TUESDAY:
				weekinfo = "Tuesday";
				break;
			case Calendar.WEDNESDAY:
				weekinfo = "Wednesday";
				break;
			case Calendar.THURSDAY:
				weekinfo = "Thursday";
				break;
			case Calendar.FRIDAY:
				weekinfo = "Friday";
				break;
			case Calendar.SATURDAY:
				weekinfo = "Saturday";
				break;
			default:
				break;
			}
		}
		return weekinfo;
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
	/**
	 * getTomorrow:(获取明天日期)
	 */
	public static Date getTomorrow(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * getYesterdayDate:(获取昨天日期).
	 *
	 * @author litao
	 * @return
	 * @since JDK 1.7
	 */
	public static String getYesterdayDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return DateUtils.sdf_yMd.format(calendar.getTime());
	}

	/**
	 * getWeekEarlyDate:(获取一周前的日期).
	 *
	 * @author litao
	 * @return
	 * @since JDK 1.7
	 */
	public static String getWeekEarlyDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		return DateUtils.sdf_yMd.format(calendar.getTime());
	}

	/**
	 * getMonthEarlyDate:(获取一月前的日期). 
	 *
	 * @author litao
	 * @return
	 * @since JDK 1.7
	 */
	public static String getMonthEarlyDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		return  DateUtils.sdf_yMd.format(calendar.getTime());
	}
	
	/**
	 * getDateByNum:(自定义获取日期(负数表示之前)). 
	 *
	 * @author litao
	 * @param num
	 * @return
	 * @since JDK 1.7
	 */
	public static Date getDateByNum(int num,Date date) {
		Calendar calendar = null;
		if(date != null){
			calendar = Calendar.getInstance();
			calendar.setTime(date);
		}else{
			calendar = Calendar.getInstance();
		}
		calendar.add(Calendar.DAY_OF_MONTH, num);
		return  calendar.getTime();
	}
	
	/**
	 * getTimeFromDates:(获取时间间隔). 
	 *
	 * @author litao
	 * @param date1
	 * @param date2
	 * @return
	 * @since JDK 1.7
	 */
	public static String getTimeFromDates(Date date1, Date date2) {
		if (date2 != null) {
			Long time = (date2.getTime() - date1.getTime()) / (60 * 60 * 1000);
			if (time < 24) {
				return time + "小时";
			} else {
				return (time / 24) + "天";
			}
		} else {
			Long time = (System.currentTimeMillis() - date1.getTime()) / (60 * 60 * 1000);
			if (time < 24) {
				return time + "小时";
			} else {
				return (time / 24) + "天";
			}
		}
	}

	/**
	 * getTimeFromDates2:(精确到分钟).
	 *
	 * @author litao
	 * @param date1
	 * @param date2
	 * @return
	 * @since JDK 1.7
	 */
	public static String getTimeFromDates2(Date date1, Date date2) {
		if (date2 != null) {
			Long timeH = (date2.getTime() - date1.getTime()) / (60 * 60 * 1000);
			if (timeH < 24) {
				if (timeH < 1) {
					Long timeM = (date2.getTime() - date1.getTime()) / (60 * 1000);
					return timeM + "分";
				} else {
					Long timeM = (date2.getTime() - date1.getTime() - timeH * (60 * 60 * 1000)) / (60 * 1000);
					return timeH + "小时" + timeM + "分";
				}
			} else {
				Long timeD = (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
				// timeH = (date2.getTime()-date1.getTime() -
				// timeD*(24*60*60*1000))/(60*60*1000);
				// Long timeM = (date2.getTime()-date1.getTime() -
				// timeD*(24*60*60*1000) - timeH*(60*60*1000))/(60*1000);
				return timeD + "天";
			}
		} else {
			Long timeH = (System.currentTimeMillis() - date1.getTime()) / (60 * 60 * 1000);
			if (timeH < 24) {
				if (timeH < 1) {
					Long timeM = (System.currentTimeMillis() - date1.getTime()) / (60 * 1000);
					return timeM + "分";
				} else {
					Long timeM = (System.currentTimeMillis() - date1.getTime() - timeH * (60 * 60 * 1000)) / (60 * 1000);
					return timeH + "小时" + timeM + "分";
				}
			} else {
				Long timeD = (System.currentTimeMillis() - date1.getTime()) / (24 * 60 * 60 * 1000);
				// timeH = (new Date().getTime()-date1.getTime() -
				// timeD*(24*60*60*1000))/(60*60*1000);
				// Long timeM = (new Date().getTime()-date1.getTime() -
				// timeD*(24*60*60*1000) - timeH*(60*60*1000))/(60*1000);
				return timeD + "天";
			}

		}
	}

	/**
	 * 获取时间段内的所有月份和年(有可能出现跨年)
	 * 
	 * @throws ParseException
	 */
	public static List<Map<String, Integer>> getMonthBetween(String minDate, String maxDate) throws ParseException {
		List<Map<String, Integer>> result = new ArrayList<Map<String, Integer>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化为年月

		Calendar min = Calendar.getInstance();
		Calendar max = Calendar.getInstance();

		min.setTime(sdf.parse(minDate));
		min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

		max.setTime(sdf.parse(maxDate));
		max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

		Calendar curr = min;
		while (curr.before(max)) {
			Map<String, Integer> map = new HashMap<>();
			int month = curr.get(Calendar.MONTH) + 1;
			int year = curr.get(Calendar.YEAR);
			map.put("month", month);
			map.put("year", year);
			result.add(map);
			curr.add(Calendar.MONTH, 1);
		}
		return result;
	}

	public static void main(String[] args) throws ParseException {

//		Date date1 = DateUtils.sdf_yMdHm.parse("2017-01-01 12:00");
//		Date date2 = DateUtils.sdf_yMdHm.parse("2017-01-02 13:00");
//		System.out.println(DateUtils.getTimeFromDates2(date1, date2));
		
//		System.out.println(DateUtils.sdf_yMdHms.format(DateUtils.getDateByNum(2,DateUtils.sdf_yMdHm.parse("2017-01-01 12:00"))));
		System.out.println(DateUtils.getMonthEarlyDate());

		// Date date1 = DateUtils.sdf_yMdHm.parse("2017-01-01 12:00");
		// Date date2 = DateUtils.sdf_yMdHm.parse("2017-01-02 13:00");
		// System.out.println(DateUtils.getTimeFromDates2(date1, date2));
//		List<Map<String, Integer>> result = getMonthBetween("2017-09-01", "2018-02-24");
		System.out.println(getTomorrow());

	}
	/**
	 * 根据当前时间获取本周内的所有日期
	 * 
=	 * @throws ParseException
	 */
	public static Map<Integer, Object> getWeekDate(Date date,String week) {
		Map<Integer, Object> dateMap = new HashMap<Integer, Object>();
		if(week.equals("星期一")) {
			for(int i=1;i<=7;i++) {
				dateMap.put(i, DateUtils.getDateByNum(i-1,date));
			}
		}else if(week.equals("星期二")) {
			for(int i=1;i<=7;i++) {
				dateMap.put(i, DateUtils.getDateByNum(i-2,date));
			}
		}else if(week.equals("星期三")) {
			for(int i=1;i<=7;i++) {
				dateMap.put(i, DateUtils.getDateByNum(i-3,date));
			}
		}else if(week.equals("星期四")) {
			for(int i=1;i<=7;i++) {
				dateMap.put(i, DateUtils.getDateByNum(i-4,date));
			}
		}else if(week.equals("星期五")) {
			for(int i=1;i<=7;i++) {
				dateMap.put(i, DateUtils.getDateByNum(i-5,date));
			}
		}else if(week.equals("星期六")) {
			for(int i=1;i<=7;i++) {
				dateMap.put(i, DateUtils.getDateByNum(i-6,date));
			}
		}else if(week.equals("星期日")) {
			for(int i=1;i<=7;i++) {
				dateMap.put(i, DateUtils.getDateByNum(i-7,date));
			}
		}
		return dateMap;
	}
	public static Integer getWeek(String week) {
		Integer i = null;
		switch (week) {
		case "星期一":
			i = 1;
			break;
		case "星期二":
			i = 2;
			break;
		case "星期三":
			i = 3;
			break;
		case "星期四":
			i = 4;
			break;
		case "星期五":
			i = 5;
			break;
		case "星期六":
			i = 6;
			break;
		case "星期日":
			i = 7;
			break;
		}
		return i;
	}
	
	/**
	 * 比较两个时间点相差是否在一周之内
	 * @param createdate
	 * @param now
	 * @return
	 */
	public static boolean CompareDate(Date createdate,Date now,int num){
		//得到日历
		Calendar calendar = Calendar.getInstance(); 
		//把当前时间赋给日历
		calendar.setTime(now);
		
		calendar.add(Calendar.DAY_OF_MONTH, num);  
		
		Date beforeNumdays = calendar.getTime();  
		
		if(beforeNumdays.getTime() < createdate.getTime()){
			return true;
		}else{
			return false;
		}
	}

}
