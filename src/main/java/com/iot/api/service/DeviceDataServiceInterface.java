package com.iot.api.service;

import java.util.List;

import com.iot.api.resources.DeviceData;
import com.iot.api.resources.InputDeviceInfo;

public interface DeviceDataServiceInterface {

	public List<DeviceData> getAllDevices();
	public DeviceData getDevice(String name);
	
	public void addEntry(DeviceData dev, InputDeviceInfo info);
	
	public void deleteEntry(String name);
	
	public List<DeviceData> getDeviceDataForLastHour();
	public List<DeviceData> getDeviceDataForLastDay();
	public List<DeviceData> getDeviceDataForLastWeek();
	public List<DeviceData> getDeviceDataForLastMonth();
	public List<DeviceData> getDeviceDataForLastYear();
}
