package com.iot.api.resources;

import java.util.Date;


public class SecondData {

	private Date secTimestamp;
	private double secTemperature;

	/**
	 * @return the secTimestamp
	 */
	public Date getSecTimestamp() {
		return secTimestamp;
	}
	/**
	 * @param secTimestamp the secTimestamp to set
	 */
	public void setSecTimestamp(Date secTimestamp) {
		this.secTimestamp = secTimestamp;
	}
	/**
	 * @return the secTemperature
	 */
	public double getSecTemperature() {
		return secTemperature;
	}
	/**
	 * @param secTemperature the secTemperature to set
	 */
	public void setSecTemperature(double secTemperature) {
		this.secTemperature = secTemperature;
	}
	
}
