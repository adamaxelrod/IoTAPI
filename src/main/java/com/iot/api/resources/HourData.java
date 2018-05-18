package com.iot.api.resources;

import java.util.*;

public class HourData {
	
	private Date hourTimestamp;
	private List<MinuteData> minData;

	public HourData() {
		minData = new ArrayList<MinuteData>();
	}
	
	/**
	 * @return the hourTimestamp
	 */
	public Date getHourTimestamp() {
		return hourTimestamp;
	}

	/**
	 * @param hourTimestamp the hourTimestamp to set
	 */
	public void setHourTimestamp(Date hourTimestamp) {
		this.hourTimestamp = hourTimestamp;
	}

	/**
	 * @return the minData
	 */
	public List<MinuteData> getMinData() {
		return minData;
	}

	/**
	 * @param minData the minData to set
	 */
	public void setMinData(List<MinuteData> minData) {
		this.minData = minData;
	}
}
