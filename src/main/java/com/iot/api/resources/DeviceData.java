package com.iot.api.resources;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.data.annotation.Id;


import com.iot.api.util.DateUtil;

@Document(collection="deviceData")
public class DeviceData {

	@Id
	private String id;
	
	private String name;

	private Date timeStamp;
	
	private YearData yearData;
	
	public DeviceData() {
		yearData = new YearData();
		timeStamp = DateUtil.getMinDate(DateUtil.getCurrDate());
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the yearData
	 */
	public YearData getYearData() {
		return yearData;
	}

	/**
	 * @param yearData the yearData to set
	 */
	public void setYearData(YearData year) {
		this.yearData = year;
	}
	
	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
