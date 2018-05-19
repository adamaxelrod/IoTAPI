package com.iot.api.service;

import java.util.List;

import com.iot.api.resources.*;

public interface DeviceDataServiceInterface {

	public List<DeviceData> getAllDevices();
	public DeviceData getDevice(String name);
	
	public void addEntry(DeviceData dev, InputDeviceInfo info);
	
	public void deleteDeviceData(String name);

	public MinuteData getDeviceDataForLastMinute(String name);
	public List<DeviceData> getDeviceDataForLastHour(String name);
	public List<DeviceData> getDeviceDataForLastDay(String name);
	public List<DeviceData> getDeviceDataForLastWeek(String name);
	public List<DeviceData> getDeviceDataForLastMonth(String name);
	public List<DeviceData> getDeviceDataForLastYear(String name);
}
