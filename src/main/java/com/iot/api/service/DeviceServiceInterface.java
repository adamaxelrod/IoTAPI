package com.iot.api.service;

import java.util.List;

import com.iot.api.resources.Device;


public interface DeviceServiceInterface {

	public List<Device> getAllDevices();
	public Device getDevice(String name);
	
	public void createDevice(Device dev);
	
	public void deleteDeviceByName(String name);	
}
