package com.iot.api.resources;

import java.util.*;

public class MonthData {

	private Date monthTimestamp;
	private List<DayData> dayData;
	
	public MonthData() {
		dayData = new ArrayList<DayData>();
	}

	/**
	 * @return the monthTimestamp
	 */
	public Date getMonthTimestamp() {
		return monthTimestamp;
	}

	/**
	 * @param monthTimestamp the monthTimestamp to set
	 */
	public void setMonthTimestamp(Date monthTimestamp) {
		this.monthTimestamp = monthTimestamp;
	}

	/**
	 * @return the dayData
	 */
	public List<DayData> getDayData() {
		return dayData;
	}

	/**
	 * @param dayData the dayData to set
	 */
	public void setDayData(List<DayData> dayData) {
		this.dayData = dayData;
	}
}
