package com.iot.api.resources;

import java.util.*;

public class DayData {

	private Date dayTimestamp;
	private List<HourData> hourData;
	
	public DayData() {
		hourData = new ArrayList<HourData>();
	}

	/**
	 * @return the dayTimestamp
	 */
	public Date getDayTimestamp() {
		return dayTimestamp;
	}

	/**
	 * @param dayTimestamp the dayTimestamp to set
	 */
	public void setDayTimestamp(Date dayTimestamp) {
		this.dayTimestamp = dayTimestamp;
	}

	/**
	 * @return the hourData
	 */
	public List<HourData> getHourData() {
		return hourData;
	}

	/**
	 * @param hourData the hourData to set
	 */
	public void setHourData(List<HourData> hourData) {
		this.hourData = hourData;
	}
}
