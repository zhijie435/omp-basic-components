package com.gb.soa.sequence.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * util型的日期数据和sql型的日期数据进行互换，并 提供日期数据的格式化输出。
 *
 * @author Fangrn
 * @since 20060802
 * @version 1.0.1
 */
public class DateUtil {
	private static final String Simple_Date_Format = "yyyy-MM-dd";
	private static final int Simple_Date_Format_Length = Simple_Date_Format.length();
	private static final String Simple_DateTime_Format = "yyyy-MM-dd HH:mm:ss";
	private static final String Chain_Simple_Date_Format = "yyyy年MM月dd日";
	protected static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	public static boolean belongCalendar(String fromWhere) {
		Integer intDateBegin = 0;
		Integer intDateEnd = 0;
		logger.info("序列号来自哪里fromWhere============" + fromWhere.trim());
		if(fromWhere.trim().equals("common")) {
			intDateBegin = 2354;
			intDateEnd = 2356;
		}else if(fromWhere.trim().equals("auto")) {
			intDateBegin = 2400;
			intDateEnd = 2402;
		}
		Boolean flag = false;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strDate = sdf.format(new Date());
			// 截取当前时间时分
			int strDateH = Integer.parseInt(strDate.substring(11, 13));
			String strDateM = strDate.substring(14, 16);
			if (strDateH < 12) {
				strDateH = strDateH + 24;
			}
			String strNow = String.valueOf(strDateH) + strDateM;
			int intNow = Integer.parseInt(strNow);
			if (intDateBegin <= intNow && intNow <= intDateEnd) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * 判断时间是否在时间段内
	 *
	 * @param nowTime
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static boolean belongCalendarDate() {
		String time1 = "23:30";
		String time2 = "24:30";
		Boolean flag = false;
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
		Date now = null;
		Date beginTime = null;
		Date endTime = null;
		try {
			now = df.parse(df.format(new Date()));
			beginTime = df.parse(time1);
			endTime = df.parse(time2);
			Calendar date = Calendar.getInstance();
			date.setTime(now);

			Calendar begin = Calendar.getInstance();
			begin.setTime(beginTime);

			Calendar end = Calendar.getInstance();
			end.setTime(endTime);

			if (date.after(begin) && date.before(end)) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;

	}

	/**
	 * 字符串转换为普通的日期
	 *
	 * @param str
	 *            日期格式Simple_Date_Format，Simple_DateTime_Format
	 * @return java.util.Date
	 */
	public static Date strToSysDate(String str) {
		if (null != str && str.length() > 0) {
			try {
				if (str.length() <= Simple_Date_Format_Length) { // 只包含日期。
					return (new SimpleDateFormat(Simple_Date_Format)).parse(str);
				} else { // 包含日期时间
					return (new SimpleDateFormat(Simple_DateTime_Format)).parse(str);
				}
			} catch (ParseException error) {
				return null;
			}
		} else
			return null;
	}

	/**
	 * 字符串转换为sql的日期
	 *
	 * @param str
	 *            String
	 * @return java.sql.Date
	 */
	public static java.sql.Date strToSqlDate(String str) {
		if (strToSysDate(str) == null || str.length() < 1)
			return null;
		else
			return new java.sql.Date(strToSysDate(str).getTime());
	}

	/**
	 * sql日期型转换为带时间的字符串
	 *
	 * @param dDate
	 *            java.sql.Date
	 * @return String "yyyy-MM-dd HH:mm:ss";
	 */
	public static String toDateTimeStr(java.sql.Date dDate) {
		if (dDate == null) {
			return null;
		} else {
			return (new SimpleDateFormat(Simple_DateTime_Format)).format(dDate);
		}
	}

	/**
	 * 普通日期型转换为带时间的字符串
	 *
	 * @param dDate
	 *            java.util.Date
	 * @return String "yyyy-MM-dd HH:mm:ss";
	 */
	public static String toDateTimeStr(Date dDate) {
		if (dDate == null) {
			return null;
		} else {
			return (new SimpleDateFormat(Simple_DateTime_Format)).format(dDate);
		}
	}

	/**
	 * 中文时间
	 *
	 * @param dDate
	 *            java.util.Date
	 * @return String "yyyy年MM月dd日";
	 */
	public static String toChainDateTimeStr(Date dDate) {
		if (dDate == null) {
			return null;
		} else {
			return (new SimpleDateFormat(Chain_Simple_Date_Format)).format(dDate);
		}
	}

	/**
	 * 中文时间
	 *
	 * @param date
	 *            java.util.String
	 * @return String "yyyy年MM月dd日";
	 */
	public static String toChainDateTimeStr(String date) {
		if (date == null || "".equals(date)) {
			return "";
		} else {
			Date d = strToSysDate(date);
			return toChainDateTimeStr(d);
		}
	}

	/**
	 * sql日期型转换为不带时间的字符串
	 *
	 * @param d
	 *            java.sql.Date
	 * @return String "yyyy-MM-dd"
	 */
	public static String toDateStr(java.sql.Date d) {
		if (d == null) {
			return null;
		} else {
			return (new SimpleDateFormat(Simple_Date_Format)).format(d);
		}
	}

	public static String toDateStr(Date d, String format) {
		return (new SimpleDateFormat(format)).format(d);
	}

	/**
	 * 普通日期型转换为不带时间的字符串
	 *
	 * @param d
	 *            java.util.Date
	 * @return String String "yyyy-MM-dd"
	 */
	public static String toDateStr(Date d) {
		if (d == null) {
			return null;
		} else {
			return (new SimpleDateFormat(Simple_Date_Format)).format(d);
		}
	}

	/**
	 * 获得当时的时间
	 *
	 * @return java.sql.Date
	 */
	public static java.sql.Date getCurrentDate() {
		return new java.sql.Date(new Date().getTime());
	}

	/**
	 * 获得当前的日期和时间（日历）
	 *
	 * @return java.util.Date
	 */
	public static Date getCurrentDateTime() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 将util型的日期型的数据转换为sql型的日期数据
	 *
	 * @param date
	 *            java.util.Date
	 * @return java.sql.Date
	 */
	public static java.sql.Date utilToSql(Date date) {
		return new java.sql.Date(date.getTime());
	}

	/**
	 * 将sql型的日期型的数据转换为util型的日期数据
	 *
	 * @param date
	 *            java.sql.Date
	 * @return java.util.Date
	 */
	public static Date sqlToUtil(java.sql.Date date) {
		return new Date(date.getTime());
	}

	/**
	 * 将日期和时间复合（组合）起来。
	 *
	 * @param date
	 *            java.sql.Date
	 * @param time
	 *            java.sql.Time
	 * @return java.sql.Date
	 */
	public static java.sql.Date compositeDateTime(java.sql.Date date, java.sql.Time time) {
		if (null == date || null == time)
			return null;
		Calendar calDate = new GregorianCalendar();
		calDate.setTimeInMillis(date.getTime());
		Calendar calTime = new GregorianCalendar();
		calTime.setTimeInMillis(time.getTime());
		Calendar calCompositeDateTime = new GregorianCalendar();
		int iYear = calDate.get(Calendar.YEAR);
		int iMonth = calDate.get(Calendar.MONTH);
		int iDay = calDate.get(Calendar.DATE);
		int iHour = calTime.get(Calendar.HOUR_OF_DAY);
		int iMin = calTime.get(Calendar.MINUTE);
		int iSec = calTime.get(Calendar.SECOND);
		int iMSec = calTime.get(Calendar.MILLISECOND);
		calCompositeDateTime.set(iYear, iMonth, iDay, iHour, iMin, iSec);
		calCompositeDateTime.set(Calendar.MILLISECOND, iMSec);
		return utilToSql(calCompositeDateTime.getTime());
	}

	/**
	 * 解析字符串型的日期型数据
	 *
	 * @param strDate
	 *            类似于：
	 * @return 解析后的日期对象
	 */
	public static Date parseDate(String strDate) {
		long r = 0;
		try {
			StringTokenizer token = new StringTokenizer(strDate, " ");
			String date = token.nextToken();
			Date now = java.sql.Date.valueOf(date);
			r = now.getTime();
			try {
				String time = token.nextToken();
				StringTokenizer tkTime = new StringTokenizer(time, ":");
				r += Integer.parseInt(tkTime.nextToken()) * 60 * 60 * 1000;
				r += Integer.parseInt(tkTime.nextToken()) * 60 * 1000;
				r += Integer.parseInt(tkTime.nextToken()) * 1000;
			} catch (Exception ex) {
				r = now.getTime();
			}
		} catch (Exception ex) {
			return new Date();
		}
		return new Date(r);
	}

	/**
	 *
	 * @return
	 */
	public static String fullTimeNoFormat() {
		return fullTimeNoFormat(new Date());
	}

	public static String fullTimeNoFormat(long date) {
		return fullTimeNoFormat(new Date(date));
	}

	public static String shortDateForChina(long date) {
		return shortDateForChina(new Date(date));
	}

	/**
	 * 日期数据格式化
	 *
	 * @param date
	 * @return yyyy 年 MM 月 dd 日
	 */
	public static String shortDateForChina(Date date) {
		String r = "";
		SimpleDateFormat formater = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
		try {
			r = formater.format(date);
		} catch (Exception ex) {
			r = formater.format(new Date());
		}
		return r;
	}

	/**
	 * 将日期数据对象格式化为字符串。
	 *
	 * @param date
	 *            java.util.Date
	 * @return 格式类似于：yyyyMMddHHmmss。
	 */
	public static String fullTimeNoFormat(Date date) {
		String r = "";
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			r = formater.format(date);
		} catch (Exception ex) {
			r = formater.format(new Date());
		}
		return r;
	}

	/**
	 * 将日期数据对象格式化为固定字符串格式。
	 *
	 * @param date
	 * @return 格式类似于：yyyy-MM-dd HH:mm:ss
	 */
	public static String fullTime(Date date) {
		String r = "";
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			r = formater.format(date);
		} catch (Exception ex) {
			r = formater.format(new Date());
		}
		return r;
	}

	/**
	 * 将当前日期数据对象格式化为固定字符串格式。
	 *
	 * @return 格式类似于：yyyy-MM-dd HH:mm:ss
	 */
	public static String fullTime() {
		return fullTime(new Date());
	}

	/**
	 * 将long日期数据对象格式化为固定字符串格式。
	 *
	 * @param long
	 * @return 格式类似于：yyyy-MM-dd HH:mm:ss
	 */
	public static String fullTime(long date) {
		return fullTime(new Date(date));
	}

	/**
	 * 将日期数据对象格式化为固定字符串格式。
	 *
	 * @param date
	 * @return 格式类似于：yyyy-MM-dd
	 */
	public static String shortDate(Date date) {
		String r = "";
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		try {
			r = formater.format(date);
		} catch (Exception ex) {
			r = "";
		}
		return r;
	}

	/**
	 * 将当前日期数据对象格式化为固定字符串格式。
	 *
	 * @return 格式类似于：yyyy-MM-dd
	 */
	public static String shortDate() {
		return shortDate(new Date());
	}

	/**
	 * 将long数据对象格式化为固定字符串格式。
	 *
	 * @param long
	 * @return 格式类似于：yyyy-MM-dd
	 */
	public static String shortDate(long date) {
		return shortDate(new Date(date));
	}

	/**
	 * 将日期数据对象格式化为固定字符串格式。
	 *
	 * @param date
	 * @return 格式类似于：HH:mm:ss
	 */
	public static String shortTime(Date date) {
		String r = "";
		SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
		try {
			r = formater.format(date);
		} catch (Exception ex) {
			r = formater.format(new Date());
		}
		return r;
	}

	/**
	 * 将当前日期数据对象格式化为固定字符串格式。
	 *
	 * @return 格式类似于：HH:mm:ss
	 */
	public static String shortTime() {
		return shortTime(new Date());
	}

	/**
	 * 将long日期数据对象格式化为固定字符串格式。
	 *
	 * @param long
	 * @return 格式类似于：HH:mm:ss
	 */
	public static String shortTime(long date) {
		return shortTime(new Date(date));
	}

	/**
	 * 获得某个月的第一天0时0分0秒的时间
	 *
	 * @param year
	 * @param month
	 * @return java.util.Date
	 */
	public static Date getFirstDateOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal.getTime();
	}

	/**
	 * 获取某个月的某一天的0时0分0秒的时间
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Calendar getFirstTimeOfDay(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return cal;
	}

	/**
	 * 将java.util.Date 转换为java.util.Calendar
	 *
	 * @param date
	 *            java.util.Date date
	 * @return java.util.Calendar
	 */
	public static Calendar DateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static Date addOneSecond(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, 1);
		return calendar.getTime();
	}

	public static Date addSecond(Date date, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}

	public static Date beforeDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -day);
		return calendar.getTime();
	}

	public static Date beforeDay(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -day);
		return calendar.getTime();
	}

	public static String getYear(Date date) {
		String r = "";
		SimpleDateFormat formater = new SimpleDateFormat("yyyy");
		try {
			r = formater.format(date);
		} catch (Exception ex) {
			r = "";
		}
		return r;
	}

	public static Boolean notEmpty(Date date) {
		if (null != date) {
			return true;
		}
		return false;
	}

	public static String getDateLong() {
		return String.valueOf(new Date().getTime());
	}

	// static String[] units = { "", "十", "百", "千", "万", "十万", "百万", "千万", "亿",
	// "十亿", "百亿", "千亿", "万亿" };
	static char[] numArray = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// int num = 245000006;
		// String numStr = foematInteger(num);
		// System.out.println("num= " + num + ", convert result: " + numStr);
		// double decimal = 245006.234206;
		// System.out.println("============================================================");
		// String decStr = formatDecimal(decimal);
		// System.out.println("decimal= " + decimal + ", decStr: " + decStr);
	}

	public static String foematInteger(int num) {
		char[] val = String.valueOf(num).toCharArray();
		int len = val.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			String m = val[i] + "";
			int n = Integer.valueOf(m);
			boolean isZero = n == 0;
			// String unit = units[(len - 1) - i];
			if (isZero) {
				if ('0' == val[i - 1]) {
					// not need process if the last digital bits is 0
					continue;
				} else {
					// no unit for 0
					sb.append(numArray[n]);
				}
			} else {
				sb.append(numArray[n]);
				// sb.append(unit);
			}
		}
		return sb.toString();
	}

	private static String formatDecimal(double decimal) {
		String decimals = String.valueOf(decimal);
		int decIndex = decimals.indexOf(".");
		int integ = Integer.valueOf(decimals.substring(0, decIndex));
		int dec = Integer.valueOf(decimals.substring(decIndex + 1));
		String result = foematInteger(integ) + "." + formatFractionalPart(dec);
		return result;
	}

	private static String formatFractionalPart(int decimal) {
		char[] val = String.valueOf(decimal).toCharArray();
		int len = val.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int n = Integer.valueOf(val[i] + "");
			sb.append(numArray[n]);
		}
		return sb.toString();
	}

}
