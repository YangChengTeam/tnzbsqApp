package com.fy.tnzbsq.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

public class DateUtils {
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 时间比较
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean compareDate(String startTime, String endTime) {
		try {
			return simpleDateFormat.parse(startTime).getTime() > simpleDateFormat
					.parse(endTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 当前时间与提供的时间比较
	 * @param dateTime
	 * @return
	 */
	public static boolean compareDateNow(String dateTime) {
		try {
			Date date = new Date();
			return date.getTime() < simpleDateFormat.parse(dateTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
}
