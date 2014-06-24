package com.planktimer.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil
{
	/**
	 * Get current time stamp.
	 * 
	 * @return the time stamp
	 */
	public static long getCurrentTimeStamp() {
		long timestamp = System.currentTimeMillis() / 1000;
		
		return timestamp;
	}
	
	/**
	 * Get current system time with format yyyy-MM-dd HH:mm:ss
	 * 
	 * @return the current time
	 */
	public static final String getCurrentTime() {
		String currentTime = new SimpleDateFormat(
				"MM-dd HH:mm:ss", Locale.getDefault()).format(
				Calendar.getInstance().getTime()).toString();
		
		return currentTime;
	}
	
	/**
	 * Fromat the given timestamp to custom string(like "today", "yestoday" and so on)
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String formatTimestamp(long timestamp) {
		Date date = new Date(timestamp * 1000L);
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss", 
				Locale.getDefault());
		String nowTime = format.format(date);
		
		return formatTimeString(nowTime);
	}
	
	/**
	 * Format the time to custom string(like "today", "yestoday" and so on)
	 * 
	 * @param  dateTime the date to transfer
	 * @return the format string
	 */
	public static String formatTimeString(String dateTime) {
		String date = "";
		String trDay = dateTime.split(" ")[0];
		String trTime = dateTime.split(" ")[1];
		trDay = trDay.substring(3, trDay.length());
		int tranDay = 0;
		try {
			tranDay = Integer.parseInt(trDay);
		} catch(NumberFormatException e) {
			trDay.substring(1, 2);
			tranDay = Integer.parseInt(trDay);
		}
		
		String currentDay = new SimpleDateFormat("dd", Locale.CHINA).format(
				Calendar.getInstance().getTime()).toString();
		int day = 0;
		try {
			day = Integer.parseInt(currentDay);
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}

		if(tranDay == day) {
			date = trTime;
		} else if(day - 1 == tranDay) {
			date = "昨天 " + trTime;
		} else if(day - 2 == tranDay) {
			date = "前天 " + trTime;
		} else {
			date = dateTime;
		}

		return date;
	}
}
