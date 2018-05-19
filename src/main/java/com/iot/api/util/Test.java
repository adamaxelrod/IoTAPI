package com.iot.api.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Test {

	private static Date localParse(Date inputDate) {
		try {
			TimeZone timeZone = TimeZone.getTimeZone("UTC");
			Calendar calendar = Calendar.getInstance(timeZone);
			calendar.setTime(inputDate);
			calendar.setTimeZone(timeZone);
	
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			simpleDateFormat.setTimeZone(timeZone);
			
			//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			//dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			
			System.out.println("ACTUAL: " + inputDate.toString());
			System.out.println("FORMATTED: " + simpleDateFormat.format(inputDate));

			return simpleDateFormat.parse(simpleDateFormat.format(inputDate));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return inputDate;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Test.localParse( Date.from(Instant.now())));
	//	System.out.println(Instant.now());
	//	System.out.println(LocalDateTime.now());
	//	System.out.println(ZonedDateTime.now());
	//	System.out.println(Date.from(Instant.now()));

	//	ZoneId zoneId = ZoneId.of("UTC");
	//	Instant instant = Instant.now();
	//	ZonedDateTime zonedDateTime = instant.atZone(zoneId);
	//	Date date = Date.from(zonedDateTime.toInstant());
	//	System.out.println(Calendar.ZONE_OFFSET);
	//	System.out.println(LocalDate.now().atTime(new Date().getHours()-Calendar., 0));
		
		
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Calendar calendar = Calendar.getInstance(timeZone);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		simpleDateFormat.setTimeZone(timeZone);

		System.out.println("default time zone: " + TimeZone.getDefault().getID());

		System.out.println("UTC:     " + simpleDateFormat.format(calendar.getTime()));
		System.out.println("Default: " + calendar.getTime());
	
	}
}
