package com.iot.api.util;

import java.util.*;

public class DateUtil {

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
}
