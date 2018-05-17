package com.iot.api.service;

import java.util.List;

import com.iot.api.resources.Device;
import com.iot.api.resources.DeviceData;

public interface DeviceServiceInterface {

	public List<Device> getAllDevices();
	public Device getDevice(String name);
	
	public void createDevice(Long id, Device dev);
	
	public Device deleteDeviceById(Long id);
	
	public List<DeviceData> getDeviceDataForLastHour();
	public List<DeviceData> getDeviceDataForLastDay();
	public List<DeviceData> getDeviceDataForLastWeek();
	public List<DeviceData> getDeviceDataForLastMonth();
	public List<DeviceData> getDeviceDataForLastYear();
}
