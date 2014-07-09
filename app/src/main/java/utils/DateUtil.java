package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil
{

	public static final int Min = 60;
	public static final int Hour = Min*60;
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

	/**
	 * second to HH:MM:SS
	 *
	 * String sec;
	 */
	public static String SecToFormat(String sec){
		StringBuffer sb = new StringBuffer();
		int hour = Integer.parseInt(sec)/Hour;
		int min = (Integer.parseInt(sec)-hour*Hour)/Min;
		int secnew = (Integer.parseInt(sec)-hour*Hour-min*Min);
		sb.append(hour);
		sb.append(":");
		sb.append(min);
		sb.append(":");
		sb.append(secnew);
		return  sb.toString();
	}

	/**
	 * get HH:MM:SS form sec
	 * @param format
	 * @return
	 */
	public static String FormatToSec(String format){
		String[] time = format.split(":");
		if (time.length != 3){
			LogUtil.e("FormatToSec:Fromat error!");
			return "";
		}else {
			int sec = Integer.parseInt(time[0])*Hour+Integer.parseInt(time[1])*Min+Integer.parseInt(time[2]);
			return Integer.toString(sec);
		}
	}

	public static String secToChinese(String sec){
		StringBuffer sb = new StringBuffer();
		int hour = Integer.parseInt(sec)/Hour;
		int min = (Integer.parseInt(sec)-hour*Hour)/Min;
		int secnew = (Integer.parseInt(sec)-hour*Hour-min*Min);
		sb.append(hour);
		sb.append("分");
		sb.append(min);
		sb.append("秒");
		sb.append(secnew);
		return  sb.toString();
	}


	public static String getDateSomeday(int before){
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.DAY_OF_MONTH,before);
		String currentTime = new SimpleDateFormat(
				"MM-dd", Locale.getDefault()).format(
				ca.getTime()).toString();
		return currentTime;
	}
}
