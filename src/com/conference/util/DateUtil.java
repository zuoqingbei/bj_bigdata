/*
 * Copyright (c) 2014. 骆驼CMS
 */

package com.conference.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.conference.common.Constant;
import com.jfinal.i18n.I18nInterceptor;

/**
 * 字符串公共工具类 <br>
 * 提供关于字符串处理的公用方法 <li>按照指定格式格式化日期并作为字符串返回 <li>将字符串数组或者容器转换为sql语句中in子句的形式。
 * 
 * @author slx
 * @date 2009-5-14 下午05:21:42
 * @version 1.0
 */
public class DateUtil {

	/**
	 * 得到日期的字符串
	 * 
	 * @author yongtree
	 * @date 2008-11-22 上午11:44:38
	 * @param date
	 * @return 日期格式：yyyy-MM-dd
	 */
	static public String getDateStr(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	static public String getDateStrOfDay(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	
	/**
	 * 获取前N天日期(返回字符串)
	 * @author ZhaDaojian 
	 * @date 2015年9月9日 上午8:40:43
	 * @param d 前几天
	 * @return
	 */
	static public String getOtherDayByString(int d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -d);//前一天
		Date date = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	
	/**
	 * 获取前N天日期(返回字Date)
	 * @author ZhaDaojian 
	 * @date 2015年9月9日 上午9:01:22
	 * @param d
	 * @return
	 */
	static public Date getOtherDayByDate(int d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -d);//前一天
		Date date = calendar.getTime();
		return date;
	}

	/**获得一天开始时间 24小时制
	 * @param date
	 * @return
	 * @author longjunfeng
	 * @date   2015年11月18日上午10:38:08
	 */
	public static  Date getDayStartTime(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		date = calendar.getTime();
		return date;
	}
	
	/**获得一天结束时间 24小时制
	 * @param date
	 * @return
	 * @author longjunfeng
	 * @date   2015年11月18日上午10:38:38
	 */
	public static  Date getDayEndTime(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		date = calendar.getTime();
		return date;
	}
	/**
	 * 得到日期的字符串
	 * 
	 * @author yongtree
	 * @date 2008-11-22 上午11:45:40
	 * @param date
	 * @param pattern
	 *            自定义格式
	 * @return
	 */
	static public String getDateStr(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		if (pattern == null || "".equals(pattern))
			pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @author yongtree
	 * @date 2008-11-22 上午11:46:13
	 * @param s
	 * @return 格式：yyyy-MM-dd
	 * @throws ParseException
	 */
	static public Date parseDate(String s) throws ParseException {
		return parseDate(s, "yyyy-MM-dd");

	}

	/**
	 * 字符串转换成日期
	 * 
	 * @author yongtree
	 * @date 2008-11-22 上午11:47:00
	 * @param s
	 * @param pattern
	 *            自定义格式
	 * @return
	 * @throws ParseException
	 */
	static public Date parseDate(String s, String pattern)
			throws ParseException {
		if (s != null && !"".equals(s)) {
			if (pattern == null || "".equals(pattern))
				pattern = "yyyy-MM-dd";
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			return format.parse(s);
		} else {
			return null;
		}

	}

	/**
	 * 字符串转换成日期
	 * 
	 * @author yongtree
	 * @date 2012-9-10下午1:51:56
	 * @param s
	 *            日期字符串
	 * @param patterns
	 *            要转化格式的数组，所以支持多种方式
	 * @param local
	 *            地区时间格式，默认为US
	 * @return
	 */
	public static Date parseDate(String s, String[] patterns, String local) {
		if (local == null || "".equals(local)) {
			local = "US";
		}
		if (s != null && !"".equals(s)) {
			Date date = null;
			for (String pattern : patterns) {
				try {
					SimpleDateFormat format = new SimpleDateFormat(pattern,
							new Locale(local));
					date = format.parse(s);
					break;
				} catch (Exception e) {
				}
			}
			return date;
		} else {
			return null;
		}
	}

	/**
	 * 得到年
	 * 
	 * @author yongtree
	 * @date 2008-11-22 上午11:47:27
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	static public int yearOfDate(Date s) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String d = format.format(s);
		return Integer.parseInt(d.substring(0, 4));
	}

	/**
	 * 得到月
	 * 
	 * @author yongtree
	 * @date 2008-11-22 上午11:47:42
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	static public int monthOfDate(Date s) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String d = format.format(s);
		return Integer.parseInt(d.substring(5, 7));
	}

	/**
	 * 得到日
	 * 
	 * @author yongtree
	 * @date 2008-11-22 上午11:47:55
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	static public int dayOfDate(Date s) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String d = format.format(s);
		return Integer.parseInt(d.substring(8, 10));
	}

	/**
	 * 得到下一个月的第一天
	 * 
	 * @author yongtree
	 * @date 2008-11-22 上午11:39:41
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	static public Date getNextMonthFirstDate(Date date) throws ParseException {
		Calendar scalendar = Calendar.getInstance();
		scalendar.setTime(date);
		scalendar.add(Calendar.MONTH, 1);
		scalendar.set(Calendar.DATE, 1);
		return new Date(scalendar.getTime().getTime());
	}

	/**
	 * 得到n分钟后的时间
	 * 
	 * @author Chengzl
	 * @date 2011-12-22下午03:02:57
	 * @param date
	 *            日期 ，
	 * @param dayCount
	 *            天数
	 * @return
	 * @throws ParseException
	 */
	public static  Date getAfterMiniterByCount(Date date, int count) {
		Calendar scalendar = Calendar.getInstance();
		scalendar.setTime(date);
		scalendar.add(Calendar.MINUTE, count);
		return new Date(scalendar.getTime().getTime());
	}

	/**
	 * 获得年月字符串组合 格式为：200902 或 2009-02 或2009/02等
	 * 
	 * @author hailang
	 * @date 2009-2-13 9:42:46
	 * @param symbol
	 *            分隔符
	 * @param zero
	 *            月份小于10的是否加入0，如01，02等
	 * @return
	 */
	public static String getThisYearMonth(String symbol, boolean zero) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;

		StringBuffer sbf = new StringBuffer();
		sbf.append(String.valueOf(year));
		if (StringUtils.isNotEmpty(symbol)) {
			sbf.append(symbol);
		}
		if (zero) {
			if (month >= 10) {
				sbf.append(String.valueOf(month));
			} else {
				sbf.append("0");
				sbf.append(String.valueOf(month));
			}
		} else {
			sbf.append(String.valueOf(month));
		}

		return sbf.toString();
	}

	/**
	 * 比较两个时间是否相等。
	 * 
	 * @author slx
	 * @date 2009-7-13 上午10:08:52
	 * @modifyNote
	 * @param d1
	 *            时间1
	 * @param d2
	 *            时间2
	 * @return 相等则true。因为数据库中读出的数据为Timestamp类型(Date的子类)，
	 *         当它与Date类型进行比较时,总是为false,即使是同一个时间.因此写了这个方法,用于兼容这两种类型的时间比较.
	 */
	public static boolean equalsDate(Date d1, Date d2) {
		if (d1 != null && d2 != null) {
			return d1.getTime() == d2.getTime();
		}
		return false;
	}

	/**
	 * 判断后面的一天是否是前面一天的下一天
	 * 
	 * @author slx
	 * @date 2009-7-8 下午04:46:38
	 * @modifyNote
	 * @param day
	 *            基准日期
	 * @param nextDay
	 *            比较日期
	 * @return 如果比较日期是基准日期的下一天则返回true，否则为false
	 */
	public static boolean isNextDay(Date day, Date nextDay) {
		return (getBetweenDays(day, nextDay) == -1);
	}

	/**
	 * 判断两个日期是否是同一天
	 * 
	 * @author slx
	 * @date 2009-11-10 下午04:32:07
	 * @modifyNote
	 * @param day
	 * @param otherDay
	 * @return
	 */
	public static boolean isSameDay(Date day, Date otherDay) {
		return (getBetweenDays(day, otherDay) == 0);
	}

	/**
	 * 计算两个日期相差的天数.不满24小时不算做一天
	 * 
	 * @author slx
	 * @date 2009-7-10 下午03:15:54
	 * @modifyNote
	 * @param fDate
	 *            日期1
	 * @param oDate
	 *            日期2
	 * @return 日期1 - 日期2 的差
	 */
	public static int getBetweenDays(Date fDate, Date sDate) {
		int day = (int) ((fDate.getTime() - sDate.getTime()) / 86400000L);// (24小时
																			// *
																			// 60分
																			// *
																			// 60秒
																			// *
																			// 1000毫秒
																			// =
																			// 1天毫秒数)
		return day;
	}
	
	public static int getBetweenMin(Date fDate, Date sDate){
		return (int) ((fDate.getTime() - sDate.getTime()) / (60*1000));
	}

	/**
	 * 日期相加指定年
	 * 
	 * @author slx
	 * @date 2009-9-10 上午10:26:22
	 * @modifyNote
	 * @param date
	 *            日期
	 * @param addYears
	 *            要添加的年数
	 * @return 相加后的日期
	 */
	public static Date addYears(Date date, int addYears) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.YEAR, addYears);
		return calender.getTime();
	}

	/**
	 * 加指定月
	 * 
	 * @author slx
	 * @date 2009-9-10 上午10:26:57
	 * @modifyNote
	 * @param date
	 *            日期
	 * @param addMonths
	 *            月数
	 * @return 相加后的日期
	 */
	public static Date addMonth(Date date, int addMonths) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.MONTH, addMonths);
		return calender.getTime();
	}

	/**
	 * 加指定天数
	 * 
	 * @author slx
	 * @date 2009-9-10 上午10:27:22
	 * @modifyNote
	 * @param date
	 *            日期
	 * @param addDays
	 *            天数
	 * @return 相加后的日期
	 */
	public static Date addDay(Date date, int addDays) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.add(Calendar.DAY_OF_YEAR, addDays);
		return calender.getTime();
	}

	/**
	 * 得到一年的第一天
	 * 
	 * @author slx
	 * @date 2009-9-10 上午11:14:23
	 * @modifyNote
	 * @param year
	 *            年
	 * @return 一年的第一天
	 */
	public static Date getFirstDateOfYear(int year) {
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.YEAR, year);
		calender.set(Calendar.DAY_OF_YEAR,
				calender.getActualMinimum(Calendar.DAY_OF_YEAR));
		setStartTimeOfDay(calender);
		return calender.getTime();
	}

	/**
	 * 得到一年的最后一天
	 * 
	 * @author slx
	 * @date 2009-9-10 上午11:14:42
	 * @modifyNote
	 * @param year
	 *            年
	 * @return 一年的最后一天
	 */
	public static Date getLastDateOfYear(int year) {
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.YEAR, year);
		calender.set(Calendar.DAY_OF_YEAR,
				calender.getActualMaximum(Calendar.DAY_OF_YEAR));
		setEndTimeOfDay(calender);
		return calender.getTime();
	}

	/**
	 * 判断当前日期是否是所在月份的最后一天
	 * 
	 * @author slx
	 * @date 2009-9-10 上午10:54:36
	 * @modifyNote
	 * @param date
	 *            日期
	 * @return 是最后一天为 true
	 */
	public static boolean isLastDayOfMonth(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		int day = calender.get(Calendar.DAY_OF_MONTH);
		int lastDay = calender.getActualMaximum(Calendar.DAY_OF_MONTH);
		return day == lastDay;
	}

	/**
	 * 得到指定月的最后一天
	 * 
	 * @author slx
	 * @date 2009-9-10 上午11:09:56
	 * @modifyNote
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 最后一天
	 */
	public static Date getLastDayOfMonth(int year, int month) {
		Calendar calender = Calendar.getInstance();
		calender.set(year, month - 1, 1);
		calender.set(Calendar.DAY_OF_MONTH,
				calender.getActualMaximum(Calendar.DAY_OF_MONTH));
		setEndTimeOfDay(calender);
		return calender.getTime();
	}

	/**
	 * 得到日期所在月的最后一天
	 * 
	 * @author slx
	 * @date 2009-9-10 上午10:54:25
	 * @modifyNote
	 * @param date
	 *            日期
	 * @return 所在月的最后一天
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.set(Calendar.DAY_OF_MONTH,
				calender.getActualMaximum(Calendar.DAY_OF_MONTH));
		setEndTimeOfDay(calender);
		return calender.getTime();
	}

	/**
	 * 设置到当前月的最后时刻
	 * 
	 * @author slx
	 * @date 2010-10-18 上午11:04:56
	 * @modifyNote
	 * @param calender
	 */
	private static void setEndTimeOfDay(Calendar calender) {
		calender.set(Calendar.HOUR_OF_DAY,
				calender.getActualMaximum(Calendar.HOUR_OF_DAY));
		calender.set(Calendar.MINUTE,
				calender.getActualMaximum(Calendar.MINUTE));
		calender.set(Calendar.SECOND,
				calender.getActualMaximum(Calendar.SECOND));
		calender.set(Calendar.MILLISECOND,
				calender.getActualMaximum(Calendar.MILLISECOND));
	}

	/**
	 * 得到指定月的第一天
	 * 
	 * @author slx
	 * @date 2009-9-10 上午11:09:56
	 * @modifyNote
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 第一天
	 */
	public static Date getFirstDayOfMonth(int year, int month) {
		Calendar calender = Calendar.getInstance();
		calender.set(year, month - 1, 1);
		calender.set(Calendar.DAY_OF_MONTH,
				calender.getActualMinimum(Calendar.DAY_OF_MONTH));
		setStartTimeOfDay(calender);
		return calender.getTime();
	}

	/**
	 * 得到指定日期所在月的第一天
	 * 
	 * @author slx
	 * @date 2009-9-10 上午11:09:56
	 * @modifyNote
	 * @param date
	 *            日期
	 * @return 第一天
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		calender.set(Calendar.DAY_OF_MONTH,
				calender.getActualMinimum(Calendar.DAY_OF_MONTH));
		setStartTimeOfDay(calender);
		return calender.getTime();
	}

	/**
	 * 设置到月份开始的时刻
	 * 
	 * @author slx
	 * @date 2010-10-18 上午11:06:12
	 * @modifyNote
	 * @param calender
	 */
	private static void setStartTimeOfDay(Calendar calender) {
		calender.set(Calendar.HOUR_OF_DAY,
				calender.getActualMinimum(Calendar.HOUR_OF_DAY));
		calender.set(Calendar.MINUTE,
				calender.getActualMinimum(Calendar.MINUTE));
		calender.set(Calendar.SECOND,
				calender.getActualMinimum(Calendar.SECOND));
		calender.set(Calendar.MILLISECOND,
				calender.getActualMinimum(Calendar.MILLISECOND));
	}

	public static Date getStartTimeOfDay(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		setStartTimeOfDay(calender);
		return calender.getTime();
	}

	public static Date getEndTimeOfDay(Date date) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		setEndTimeOfDay(calender);
		return calender.getTime();

	}

	/**
	 * 得到当前年月
	 * 
	 * @author yongtree
	 * @date 2008-11-22 上午11:25:24
	 * @return 格式：2008-11
	 * @throws ParseException
	 */
	public static String getThisYearMonth() throws ParseException {
		return getYearMonth(new Date());
	}

	/**
	 * 得到年月
	 * 
	 * @author slx
	 * @date 2010年4月16日13:09:23
	 * @return 格式：2008-11
	 * @throws ParseException
	 */
	public static String getYearMonth(Date date) {
		Calendar today = Calendar.getInstance();
		today.setTime(date);
		return (today.get(Calendar.YEAR))
				+ "-"
				+ ((today.get(Calendar.MONTH) + 1) >= 10 ? (today
						.get(Calendar.MONTH) + 1) : ("0" + (today
						.get(Calendar.MONTH) + 1)));
	}

	/**
	 * 得到当前年份
	 * 
	 * @author Chengzl
	 * @date 2011-7-9上午09:48:10
	 * @return
	 */
	public static Integer getYear() {
		Date date = new Date();
		Calendar nowDate = Calendar.getInstance();
		nowDate.setTime(date);
		return nowDate.get(Calendar.YEAR);

	}

	/**
	 * 得到当前月份
	 * 
	 * @author Chengzl
	 * @date 2011-7-9上午09:48:10
	 * @return
	 */
	public static Integer getMonth() {
		Date date = new Date();
		Calendar nowDate = Calendar.getInstance();
		nowDate.setTime(date);
		return Integer
				.valueOf(((nowDate.get(Calendar.MONTH) + 1) >= 10 ? (nowDate
						.get(Calendar.MONTH) + 1) : ("0" + (nowDate
						.get(Calendar.MONTH) + 1))).toString());

	}

	/**
	 * 计算两个日期之间相差的月份数 <br>
	 * 日期顺序不分先后不会返回负数 <br>
	 * 不足一个月不算做一个月
	 * 
	 * @author slx
	 * @date 2010年4月16日11:32:51
	 * @modifyNote
	 * @param date1
	 *            日期1
	 * @param date2
	 *            日期2
	 * @return 月数
	 */
	public static int getBetweenMonths(Date date1, Date date2) {
		int iMonth = 0;
		int flag = 0;
		Calendar objCalendarDate1 = Calendar.getInstance();
		objCalendarDate1.setTime(date1);

		Calendar objCalendarDate2 = Calendar.getInstance();
		objCalendarDate2.setTime(date2);

		if (objCalendarDate2.equals(objCalendarDate1))
			return 0;
		if (objCalendarDate1.after(objCalendarDate2)) {
			Calendar temp = objCalendarDate1;
			objCalendarDate1 = objCalendarDate2;
			objCalendarDate2 = temp;
		}
		if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1
				.get(Calendar.DAY_OF_MONTH))
			flag = 1;

		if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1
				.get(Calendar.YEAR))
			iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1
					.get(Calendar.YEAR))
					* 12
					+ objCalendarDate2.get(Calendar.MONTH) - flag)
					- objCalendarDate1.get(Calendar.MONTH);
		else
			iMonth = objCalendarDate2.get(Calendar.MONTH)
					- objCalendarDate1.get(Calendar.MONTH) - flag;

		return iMonth;
	}

	/**
	 * 计算两个日期之间相差的年份数 <br>
	 * 日期顺序不分先后不会返回负数 <br>
	 * 不足一个年不算做一个年
	 * 
	 * @author slx
	 * @date 2010年4月16日12:01:46
	 * @modifyNote
	 * @param date1
	 *            日期1
	 * @param date2
	 *            日期2
	 * @return 年数
	 */
	public static int getBetweenYears(Date date1, Date date2) {
		return getBetweenMonths(date1, date2) / 12;
	}

	/**
	 * 判断当前日期是星期几
	 * 
	 * @author YangTao
	 * @date 2012-11-19
	 * @desc
	 */
	public static int dayForWeek(Date date) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 得到n天前的日期
	 * 
	 * @author Administrator
	 * @date 2012-12-6上午10:30:55
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date getBeforeDateByDay(Date date, int count) {
		Calendar scalendar = Calendar.getInstance();
		scalendar.setTime(date);
		scalendar.add(Calendar.HOUR, -(count * 24));
		return new Date(scalendar.getTime().getTime());
	}
	
	/**  
     * 取得季度最后一天  
     *  
     * @param date  
     * @return  
     */  
    public static Date getLastDateOfSeason(Date date) {   
        return getLastDayOfMonth(getSeasonDate(date)[2]);   
    } 
    
    /**  
     * 取得季度月  
     *  
     * @param date  
     * @return  
     */  
    public static Date[] getSeasonDate(Date date) {   
        Date[] season = new Date[3];   
  
        Calendar c = Calendar.getInstance();   
        c.setTime(date);   
  
        int nSeason = getSeason(date);   
        if(nSeason == 1) {//第一季度   
            c.set(Calendar.MONTH, Calendar.JANUARY);   
            season[0] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.FEBRUARY);   
            season[1] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.MARCH);   
            season[2] = c.getTime();   
        } else if(nSeason == 2) {//第二季度   
            c.set(Calendar.MONTH, Calendar.APRIL);   
            season[0] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.MAY);   
            season[1] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.JUNE);   
            season[2] = c.getTime();   
        } else if(nSeason == 3) {//第三季度   
            c.set(Calendar.MONTH, Calendar.JULY);   
            season[0] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.AUGUST);   
            season[1] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.SEPTEMBER);   
            season[2] = c.getTime();   
        } else if(nSeason == 4) {//第四季度   
            c.set(Calendar.MONTH, Calendar.OCTOBER);   
            season[0] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.NOVEMBER);   
            season[1] = c.getTime();   
            c.set(Calendar.MONTH, Calendar.DECEMBER);   
            season[2] = c.getTime();   
        }   
        return season;   
    } 
	 /**  
     *  根据日期判断所在季度数
     * 1 第一季度  2 第二季度 3 第三季度 4 第四季度  
     *  
     * @param date  
     * @return  
     */  
    public static int getSeason(Date date) {   
  
        int season = 0;   
  
        Calendar c = Calendar.getInstance();   
        c.setTime(date);   
        int month = c.get(Calendar.MONTH);   
        switch (month) {   
            case Calendar.JANUARY:   
            case Calendar.FEBRUARY:   
            case Calendar.MARCH:   
                season =  1;   
                break;   
            case Calendar.APRIL:   
            case Calendar.MAY:   
            case Calendar.JUNE:   
                season =  2;   
                break;   
            case Calendar.JULY:   
            case Calendar.AUGUST:   
            case Calendar.SEPTEMBER:   
                season =  3;   
                break;   
            case Calendar.OCTOBER:   
            case Calendar.NOVEMBER:   
            case Calendar.DECEMBER:   
                season =  4;   
                break;   
            default:   
                break;   
        }   
        return season;   
    }  

    /**
     * 
     * @param date		日期
     * @param format	格式
     * @return
     * @throws ParseException
     */
    public static String format(Date date,String format){
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
		String str = sdf.format(date);
		return str;
    }
    
    
    /** 
     * @Desc: 中英文日期转换
     * @param date
     * @param cnFormat
     * @param enFormat
     * @param language
     * @return
     * @return: String
     * @author: longjunfeng   
     * @date: 2016年11月23日 下午6:36:48 
     */
    public static String i18nFormat(Date date,String cnFormat,String enFormat,String language){
    	Calendar calender = Calendar.getInstance();
		calender.setTime(date);
    	if(Constant.I18N_ENGLISH.equals(language)){
    		SimpleDateFormat sdf = new SimpleDateFormat(enFormat,  Locale.ENGLISH);
    		return sdf.format(calender.getTime());
    	}else if(Constant.I18N_CHINESE.equals(language)){
    		SimpleDateFormat sdf = new SimpleDateFormat(cnFormat,  Locale.CHINESE);
    		return sdf.format(calender.getTime());
    	}
    	return "";
    }
    
	public static void main(String[] args) throws Exception {
		// Date d1 = parseToDate("2009-11-29", null);
		// Date d2 = parseToDate("2007-12-29", null);
		// System.out.println(formatDate(getFirstDayOfMonth(2010,10),"yyyy-MM-dd HH:mm:ss.SSS"));
		//
		// System.out.println(formatDate(getLastDateOfYear(2009),"yyyy-MM-dd HH:mm:ss.SSS"));
		// System.out.println(formatDate(getFirstDateOfYear(2009),"yyyy-MM-dd HH:mm:ss.SSS"));
		// System.out.println(formatDate(getEndTimeOfDay(new
		// Date()),"yyyy-MM-dd HH:mm:ss.SSS"));

		// System.out.println(DateUtil.parseDate("2012-Sep-06",
		// "yyyy-MMMMM-dd"));
		// String[] patterns = { "yyyy-MM-dd", "yyyy-MMMMM-dd", "MMMMM.dd,yyyy",
		// "MMMMM dd,yyyy" };
		// String local = "US";
		// String[] dates = { "2012-02-03", "2012-2-3", "2012-Sep-06",
		// "September 04, 2012", "Sep.6,2012" };
		// for (String date : dates) {
		// System.out.println(getDateStr(parseDate(date, patterns, local),
		// "yyyy年MM月dd日"));
		// }
//		System.out.println(getBeforeDateByDay(new Date(), 7));
//		System.out.println(new Date());
		
		Calendar calender = Calendar.getInstance();
		calender.set(2016, 10, 10);
//		calender.set(Calendar.DAY_OF_MONTH,
//				calender.getActualMaximum(Calendar.DAY_OF_MONTH));
//		setEndTimeOfDay(calender);
		System.out.println(Locale.ENGLISH);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy",  
                Locale.ENGLISH);  
		System.out.println(sdf.format(calender.getTime()));  
		System.out.println(calender.getTime().toString());
		
	}
}
