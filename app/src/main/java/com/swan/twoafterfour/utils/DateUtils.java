package com.swan.twoafterfour.utils;

import android.content.Context;
import android.util.Log;

import com.swan.twoafterfour.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by TongYuliang on 2017/5/7.
 * 时间工具
 */

public class DateUtils {
	private final String TAG = "DateUtils";

	/**
	 * 根据当前日期获得是星期几
	 *
	 * @return
	 */
	public String getWeek(String time) {
		String Week = "";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			Week += "周日";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			Week += "周一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
			Week += "周二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
			Week += "周三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
			Week += "周四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
			Week += "周五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			Week += "周六";
		}
		return Week;
	}

	/**
	 * 获取今天往后一周的日期
	 */
	public List<Date> getSevenDates() {
		int year; // 当前年
		int month; // 月
		int day; // 日
		List<Date> dates = new ArrayList<>();

		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//		c.clear();
//		c.set(Calendar.YEAR, 2016); // 测试
//		c.set(Calendar.MONTH, 1); // 测试，注意：Calendar对象默认一月为0
//		c.set(Calendar.DAY_OF_MONTH, 26); // 测试
//		year = c.get(Calendar.YEAR);// 获取当前年份
//		month = c.get(Calendar.MONTH) + 1;// 获取当前月份
//		day = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期
		dates.add(c.getTime());
		for (int i = 0; i < 6; i++) {
			c.add(Calendar.DATE, 1); // 把日期往后增加一天.整数往后推,负数往前移动
//			year = c.get(Calendar.YEAR); // 获取当前年份
//			month = c.get(Calendar.MONTH) + 1; // 获取当前月份
//			day = c.get(Calendar.DAY_OF_MONTH); // 获取当前月份的日期号码
			dates.add(c.getTime());
		}
		return dates;
	}

	/**
	 * 获取今天往前一个月的日期
	 */
	private List<Date> getMonths(int amount) {
		List<Date> dates = new ArrayList<>();

		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		dates.add(c.getTime());
		for (int i = 0; i < amount - 1; i++) {
			c.add(Calendar.MONTH, -1); // 把日期往前减一月.整数往后推,负数往前移动
			dates.add(c.getTime());
		}
		return dates;
	}

	/**
	 * 获取最近12个月的月份
	 *
	 * @param context
	 * @return
	 */
	public List<String> getTwelveMonths(Context context) {
		List<Date> dates = getMonths(12);
		return changeDateToString(context, dates);
	}

	/**
	 * 获取最近12个月的月份（yyyy-MM）
	 *
	 * @return
	 */
	public List<String> formatTwelveMonths() {
		List<Date> dates = getMonths(12);
		return formatMonths(dates);
	}

	/**
	 * 格式化月份（yyyy-MM）
	 *
	 * @return
	 */
	private List<String> formatMonths(List<Date> dates) {
		List<String> formatMonths = new ArrayList<>();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
		for (Date date : dates) {
			formatMonths.add(simpleDateFormat.format(date));
		}

		return formatMonths;
	}

	/**
	 * 转换月份格式（yyyy-MM → yyyy年MM月）
	 *
	 * @param formatMonths
	 * @return
	 */
	public List<String> changeMonthsFormat(Context context, List<String> formatMonths) {
		List<Date> dates = changeStringToDate(formatMonths);
		return changeDateToString(context, dates);
	}

	/**
	 * Date 转 String（yyyy年MM月）
	 *
	 * @param context
	 * @param dates
	 * @return
	 */
	private List<String> changeDateToString(Context context, List<Date> dates) {
		List<String> datesStrings = new ArrayList<>();

		int year; // 当前年
		int month; // 月
		int day; // 日
		String dateString;
		Calendar c = Calendar.getInstance();
		for (Date date :
				dates) {
			c.setTime(date);
			year = c.get(Calendar.YEAR);// 获取当前年份
			month = c.get(Calendar.MONTH) + 1;// 获取当前月份
			day = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期
			Log.d(TAG, "day:" + day);
			dateString = String.format(context.getString(R.string.date_year_month), year, month);
			datesStrings.add(dateString);
		}

		return datesStrings;
	}

	/**
	 * String（yyyy-MM）转 Date
	 *
	 * @param formatMonths
	 * @return
	 */
	private List<Date> changeStringToDate(List<String> formatMonths) {
		List<Date> dates = new ArrayList<>();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
		try {
			for (String month : formatMonths) {
				dates.add(simpleDateFormat.parse(month));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dates;
	}

	/**
	 * 分钟转“天 小时 分钟”格式
	 *
	 * @param context
	 * @param minutes
	 * @return
	 */
	public String formatDayHourMinute(Context context, int minutes) {
		StringBuilder string = new StringBuilder();
		int days = minutes / (60 * 24);
		int hours = minutes % (60 * 24) / 60;
		if (days > 0) {
			string.append(days).append(context.getString(R.string.day));
		}
		if (hours > 0) {
			string.append(hours).append(context.getString(R.string.hour));
		}
		string.append(minutes % (60 * 24) % 60).append(context.getString(R.string.minute));

		return string.toString();
	}

	/**
	 * 分钟转“小时 分钟”格式，区分正负值
	 *
	 * @param context
	 * @param minutes
	 * @return
	 */
	public String formatHourMinute(Context context, int minutes) {
		StringBuilder string = new StringBuilder();
		if (minutes > 0) {
			string.append("+");
		} else if (minutes < 0) {
			string.append("-");
		}
		minutes = Math.abs(minutes);
		int hours = minutes / 60;
		if (hours > 0) {
			string.append(hours).append(context.getString(R.string.hour));
		}
		string.append(minutes % 60).append(context.getString(R.string.minute));

		return string.toString();
	}

	/**
	 * 判断是否超时
	 *
	 * @return true：超时；false：未超时
	 */
	public boolean isOvertime(long time) {
		Calendar calendar = Calendar.getInstance();
		// 当前时间
		long currentTime = calendar.getTimeInMillis();
		Log.d(TAG, "currentTime - endTime:" + (currentTime - time));
		return currentTime >= time;
	}

	/**
	 * 时间字符串转时间戳
	 *
	 * @param time 时间字符串（"yyyy-MM-dd HH:mm:ss"）
	 * @return 时间戳
	 */
	public long formatTimeInMillis(String time) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
					.parse(time));
			return calendar.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 时间戳转换成日期格式字符串
	 *
	 * @param time   时间戳
	 * @param format
	 * @return
	 */
	public static String timeStamp2Date(long time, String format) {
		if (time == 0) {
			return "";
		}
		if (format == null || format.isEmpty()) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(time));
	}

	/**
	 * 日期格式字符串转换成时间戳
	 *
	 * @param date   字符串日期
	 * @param format 如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static long date2TimeStamp(String date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(date).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


}
