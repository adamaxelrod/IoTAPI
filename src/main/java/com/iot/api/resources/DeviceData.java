package com.iot.api.resources;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.annotation.Id;


import com.iot.api.util.DateUtil;

@Document(collection="deviceData")
public class DeviceData {

	@Id
	private String id;
	
	private String name;

	private Date timeStamp;
	
	private double temperature;
	
	private MinuteData minData;
	
	public DeviceData() {
		minData = new MinuteData();
		timeStamp = DateUtil.getMinDate(new Date());
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
	 * @return the minData
	 */
	public MinuteData getMin() {
		return minData;
	}

	/**
	 * @param minData the minData to set
	 */
	public void setMin(MinuteData min) {
		this.minData = min;
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
	/**
	 * @return the temperature
	 */
	public double getTemperature() {
		return temperature;
	}
	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
}
