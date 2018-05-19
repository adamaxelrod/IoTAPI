package com.iot.api.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

public class DateUtil {

	public static Date getCurrDate() { 
		return  Date.from(Instant.now());		
	}
	
	public static Date getCurrDateUTC() { 
		return  Date.from(Instant.now());		
	}
	
	/**
	 * Custom date class to get different timestamps and ensure common timezone
	 * @param date
	 * @return
	 */
	public static Date getYearData(Date date) {
		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		return localParse(calendar.getTime());		    	
	}
	
	public static Date getMonthDate(Date date) {
		Calendar calendar = Calendar.getInstance();
	//	calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		return localParse(calendar.getTime());	    	
	}
	
	public static Date getDayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		return localParse(calendar.getTime());		    	
	}
	
	public static Date getHourDate(Date date) {
		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, date.getHours());
		return localParse(calendar.getTime());		    	
	}
	
	public static Date getMinDate(Date date) {
		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, date.getMinutes());
		calendar.set(Calendar.HOUR, date.getHours());
		return localParse(calendar.getTime());		    	
	}
	
	public static Date getSecDate(Date date) {
		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, date.getSeconds());
		calendar.set(Calendar.MINUTE, date.getMinutes());
		calendar.set(Calendar.HOUR, date.getHours());
		return localParse(calendar.getTime());	    	
	}

	public static Date getFullDate(Date date) {
		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		calendar.setTime(date);
		calendar.set(Calendar.SECOND, date.getSeconds());
		calendar.set(Calendar.MINUTE, date.getMinutes());
		calendar.set(Calendar.HOUR, date.getHours());
		return localParse(calendar.getTime());		    	
	}
	
	public static String parseDateAsString(Date inputDate) {
		try {
			TimeZone timeZone = TimeZone.getTimeZone("UTC");
			Calendar calendar = Calendar.getInstance(timeZone);
			calendar.setTime(inputDate);
			calendar.setTimeZone(timeZone);
	
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			simpleDateFormat.setTimeZone(timeZone);
						
			System.out.println("FORMATTED: " + simpleDateFormat.format(inputDate));

			return simpleDateFormat.format(inputDate);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return inputDate.toString();
		}
	}
	
	private static Date localParse(Date inputDate) {
		try {
			TimeZone timeZone = TimeZone.getTimeZone("UTC");
			Calendar calendar = Calendar.getInstance(timeZone);
			calendar.setTime(inputDate);
			calendar.setTimeZone(timeZone);
	
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			simpleDateFormat.setTimeZone(timeZone);
			
			System.out.println("ACTUAL: " + inputDate.toString());
			System.out.println("FORMATTED: " + simpleDateFormat.format(inputDate));

			return simpleDateFormat.parse(simpleDateFormat.format(inputDate));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return inputDate;
		}
	}
}
