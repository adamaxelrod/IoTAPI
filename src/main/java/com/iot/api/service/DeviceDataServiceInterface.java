package com.iot.api.service;

import java.util.List;

import org.json.simple.JSONObject;

import com.iot.api.resources.*;

public interface DeviceDataServiceInterface {

	public List<DeviceData> getAllDevices();
	public DeviceData getDevice(String name);
	
	public void addEntry(DeviceData dev, InputDeviceInfo info);
	
	public void deleteDeviceData(String name);

	public JSONObject getDeviceDataForLastMinute(String name);
	public JSONObject getDeviceDataForLastHour(String name);
	public JSONObject getDeviceDataForLastDay(String name);
	public JSONObject getDeviceDataForLastWeek(String name);
	public JSONObject getDeviceDataForLastMonth(String name);
	public JSONObject getDeviceDataForLastYear(String name);
}
