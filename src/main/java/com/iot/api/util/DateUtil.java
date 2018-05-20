package com.iot.api.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
	//Default Logging using log4j2
	public static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
		
	public static Date getCurrDate() { 
		return  Date.from(Instant.now());		
	}
	
	/**
	 * Custom date class to get different timestamps and ensure common timezone
	 * @param date
	 * @return
	 */
	public static Date getYearDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		return localParse(calendar.getTime());		    	
	}
	
	public static Date getMonthDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return localParse(calendar.getTime());	    	
	}
	
	public static Date getDayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		return localParse(calendar.getTime());		    	
	}
	
	public static Date getHourDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, date.getHours());
		return localParse(calendar.getTime());		    	
	}
	
	public static Date getMinDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, date.getMinutes());
		calendar.set(Calendar.HOUR, date.getHours());
		return localParse(calendar.getTime());		    	
	}
	
	public static Date getSecDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, date.getSeconds());
		calendar.set(Calendar.MINUTE, date.getMinutes());
		calendar.set(Calendar.HOUR, date.getHours());
		return localParse(calendar.getTime());	    	
	}

	public static Date getFullDate(Date date) {
		Calendar calendar = Calendar.getInstance();
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
			
			return simpleDateFormat.format(inputDate);
		}
		catch(Exception e) {
			logger.error(e.getMessage(), e);
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
			
			logger.debug("Returning Date: " + simpleDateFormat.format(inputDate));
			return simpleDateFormat.parse(simpleDateFormat.format(inputDate));
		}
		catch(Exception e) {
			logger.error(e.getMessage(), e);
			return inputDate;
		}
	}
}
