package com.etocrm.sdk.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtils {

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}


	public static Date getstrDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date strtodate = formatter.parse(strDate);
			return strtodate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 日期转换成时间
	 * */
	public static String  getDateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String  dateToString = formatter.format(date);
			return dateToString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 日期字符串转换成时间
	 * */
	public static Date  dateStringToDate(String date,String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			Date stringToDate = formatter.parse(date);
			return stringToDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据开始结束日期获取每天日期
	 * begDate,endDate格式为yyyy-MM-dd
	 * */
	public static List<Date>  findDate(Date begDate,Date endDate) {
		List<Date>list=new ArrayList<>();
		try {
			list.add(begDate);
			Calendar calendarBagDate=Calendar.getInstance();
			calendarBagDate.setTime(begDate);
			Calendar calendarEndDate=Calendar.getInstance();
			calendarEndDate.setTime(endDate);
			while (calendarEndDate.after(calendarBagDate)){
				calendarBagDate.add(Calendar.DAY_OF_MONTH,1);
				list.add(calendarBagDate.getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String getGapTime(long time){
		long hours = time / (1000 * 60 * 60);
		long minutes = (time-hours*(1000 * 60 * 60 ))/(1000* 60);
		long second = (time-hours*(1000 * 60 * 60 )-minutes*(1000 * 60 ))/1000;
		String diffTime="";
		if(minutes<10){
			diffTime=hours+":0"+minutes;
		}else{
			diffTime=hours+":"+minutes;
		}
		if(second<10){
			diffTime=diffTime+":0"+second;
		}else{
			diffTime=diffTime+":"+second;
		}
		return diffTime;
	}

	public static String getString(Long time) {
		Date date = new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	public static String getDateString() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	/**
	 * @Description: 获取某个时间段内所有的日期
	 * @author xing.liu
	 */
	public static List<String> findDates(Date dBegin, Date dEnd){
		List<String> lDate = new ArrayList<String>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		lDate.add(sd.format(dBegin));
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime()))
		{
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(sd.format(calBegin.getTime()));
		}
		return lDate;
	}

    /**
     * @Description:日期增加一天或多天
     * @author xing.liu
     * @date ${DATE} ${TIME}
     */
	public static String addDayone(String  date,Integer... day){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(getstrDate(date));
		if(day.length==0){
			c.add(Calendar.DAY_OF_MONTH, 1);
		}else{
			c.add(Calendar.DAY_OF_MONTH, day[0]);

		}
		Date tomorrow = c.getTime();
		return f.format(tomorrow);
	}

	public static String getYyyyMMdd(Long time) {
		Date date = new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	/****
	 * 传入具体日期 ，返回具体日期减少一天
	 * @return 2017-04-12
	 * @throws ParseException
	 */
	public static  String subDay(Integer ...day)  {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = new  Date();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.DAY_OF_MONTH, -1);
		Date dt1 = rightNow.getTime();
		String reStr = sdf.format(dt1);
		return reStr;
	}

	public static String reduceDayone(String  date,Integer day){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(getstrDate(date));
		c.add(Calendar.DAY_OF_MONTH, day);
		Date tomorrow = c.getTime();
		return f.format(tomorrow);
	}


}
