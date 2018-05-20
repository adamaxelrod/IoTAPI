package com.iot.api.service;

import java.util.List;

import org.json.simple.JSONArray;

import com.iot.api.resources.*;

public interface DeviceDataServiceInterface {

	public List<DeviceData> getAllDevices();
	public DeviceData getDevice(String name);
	
	public void addEntry(DeviceData dev, InputDeviceInfo info);
	
	public void deleteDeviceData(String name);

	public JSONArray getDeviceDataForLastMinute(String name);
	public JSONArray getDeviceDataForLastHour(String name);
	public JSONArray getDeviceDataForLastDay(String name);
	public JSONArray getDeviceDataForLastWeek(String name);
	public JSONArray getDeviceDataForLastMonth(String name);
	public JSONArray getDeviceDataForLastYear(String name);
}
