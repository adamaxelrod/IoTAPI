package com.iot.api.util;

import java.util.*;

public class DateUtil {
	/**
	 * Custom date class to get different timestamps and ensure common timezone
	 * @param date
	 * @return
	 */
	public static Date getYearData(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		return calendar.getTime();		    	
	}
	
	public static Date getMonthDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		return calendar.getTime();		    	
	}
	
	public static Date getDayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		return calendar.getTime();		    	
	}
	
	public static Date getHourDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, date.getHours());
		return calendar.getTime();		    	
	}
	
	public static Date getMinDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, date.getMinutes());
		calendar.set(Calendar.HOUR, date.getHours());
		return calendar.getTime();		    	
	}
	
	public static Date getSecDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, date.getSeconds());
		calendar.set(Calendar.MINUTE, date.getMinutes());
		calendar.set(Calendar.HOUR, date.getHours());
		return calendar.getTime();		    	
	}

	public static Date getFullDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.SECOND, date.getSeconds());
		calendar.set(Calendar.MINUTE, date.getMinutes());
		calendar.set(Calendar.HOUR, date.getHours());
		return calendar.getTime();		    	
	}
}
