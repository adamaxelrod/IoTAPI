package com.iot.api.resources;

import java.util.*;

public class YearData {

	private Date yearTimestamp;
	private List<MonthData> monthData;
	
	public YearData() {
		monthData = new ArrayList<MonthData>();
	}

	/**
	 * @return the yearTimestamp
	 */
	public Date getYearTimestamp() {
		return yearTimestamp;
	}

	/**
	 * @param yearTimestamp the yearTimestamp to set
	 */
	public void setYearTimestamp(Date yearTimestamp) {
		this.yearTimestamp = yearTimestamp;
	}

	/**
	 * @return the monthData
	 */
	public List<MonthData> getMonthData() {
		return monthData;
	}

	/**
	 * @param monthData the monthData to set
	 */
	public void setMonthData(List<MonthData> monthData) {
		this.monthData = monthData;
	}
}
