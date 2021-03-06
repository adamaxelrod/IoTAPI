package com.iot.api.resources;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.util.Date;
import java.util.ArrayList;


public class MinuteData {

	@DateTimeFormat(iso=ISO.DATE_TIME)
	private Date minTimestamp;
	
	private List<SecondData> secData;
	
	public MinuteData() {
		secData = new ArrayList<SecondData>();
	}
	
	/**
	 * @return the minTimestamp
	 */
	public Date getMinTimestamp() {
		return minTimestamp;
	}
	/**
	 * @param minTimestamp the minTimestamp to set
	 */
	public void setMinTimestamp(Date minTimestamp) {
		this.minTimestamp = minTimestamp;
	}
	/**
	 * @return the secData
	 */
	public List<SecondData> getSecData() {
		return secData;
	}
	/**
	 * @param secData the secData to set
	 */
	public void setSecData(List<SecondData> secData) {
		this.secData = secData;
	}

}
