package com.etocrm.sdk.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtils {


	public  static final String DATE_FORMAT="yyyy-MM-dd";
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate(DATE_FORMAT);
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}


	public static Date getstrDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
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
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
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
		long hours = time / ( 60 * 60);
		long minutes = (time-hours*(60 * 60 ))/( 60);
		long second = (time-hours*( 60 * 60 )-minutes*( 60 ));
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
		SimpleDateFormat sd = new SimpleDateFormat(DATE_FORMAT);
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
		SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT);
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
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		return formatter.format(date);
	}

	/****
	 * 传入具体日期 ，返回具体日期减少一天
	 * @return 2017-04-12
	 * @throws ParseException
	 */
	public static  String subDay(Integer ...day)  {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date dt = new  Date();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.DAY_OF_MONTH, -1);
		Date dt1 = rightNow.getTime();
		String reStr = sdf.format(dt1);
		return reStr;
	}

	public static String reduceDayone(String  date,Integer day){
		SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT);
		Calendar c = Calendar.getInstance();
		c.setTime(getstrDate(date));
		c.add(Calendar.DAY_OF_MONTH, day);
		Date tomorrow = c.getTime();
		return f.format(tomorrow);
	}

	public static  Date getYestDay()  {
		Date dt = new  Date();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.DAY_OF_MONTH, -1);
		Date dt1 = rightNow.getTime();
		return dt1;
	}

	public static  String getYestDayString()  {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date dt = new  Date();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.DAY_OF_MONTH, -1);
		Date dt1 = rightNow.getTime();
		String reStr = sdf.format(dt1);
		return reStr;
	}

	public  static  String getRedMonth(String endDate ){
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDate beginDateTime = LocalDate.parse(endDate, fmt);
		LocalDate localDate = beginDateTime.minusMonths(1);
		String month = localDate.format(fmt);
		return month;
	}

	public  static  String getAddMonth(String endDate ){
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDate beginDateTime = LocalDate.parse(endDate, fmt);
		LocalDate localDate = beginDateTime.plusMonths(1);
		String month = localDate.format(fmt);
		return month;
	}
	public  static  String getReduceMonth(String endDate ){
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDate beginDateTime = LocalDate.parse(endDate, fmt);
		LocalDate localDate = beginDateTime.plusMonths(-1);
		String month = localDate.format(fmt);
		return month;
	}

	public  static String getMonday(String daystr){
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDate date = LocalDate.parse(daystr, fmt);
		LocalDate day2=date.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusDays(1);
		return day2.toString();
	}

	public  static String getSunday(String daystr){
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDate date = LocalDate.parse(daystr, fmt);
		LocalDate day2=date.minusDays(7);
		return day2.toString();
	}

	public static String getPreviousDay(int day) {
		//获取今天日期
		LocalDate now = LocalDate.now();
		//获取上周的今天
		LocalDate minusDays = now.minusDays(7L);
		//获取上周的周几
		LocalDate result = minusDays.with(DayOfWeek.of(day));
		//日期按照“yyyy-MM-dd”格式化
		return result.format(DateTimeFormatter.ISO_DATE);
	}
	public static String getPreviousDay(String daystr, Integer day) {
		//获取今天日期
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDate date = LocalDate.parse(daystr, fmt);		//获取上周的今天
		LocalDate minusDays = date.minusDays(7L);
		//获取上周的周几
		LocalDate result = minusDays.with(DayOfWeek.of(day));
		//日期按照“yyyy-MM-dd”格式化
		return result.format(DateTimeFormatter.ISO_DATE);
	}

	/**
	 *
	 * @Description 当前时间前半个小时
	 * @author xing.liu
	 * @date 2021/6/7
	 **/
	public  static String getBeforehalfHour(){
		Date date1=new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		cal.add(Calendar.MINUTE, -30);
		Date d2 = cal.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(d2);
	}








}
