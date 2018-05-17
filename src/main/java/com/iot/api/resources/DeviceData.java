package com.iot.api.resources;

import java.util.Date;

public class DeviceData {

	private Device devInfo;
	private Date timeStamp;
	private double temperature;
	
	public DeviceData() {
		
	}
	
	/**
	 * @return the devInfo
	 */
	public Device getDevInfo() {
		return devInfo;
	}
	/**
	 * @param devInfo the devInfo to set
	 */
	public void setDevInfo(Device devInfo) {
		this.devInfo = devInfo;
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
