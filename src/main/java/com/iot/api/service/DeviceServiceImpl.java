package com.iot.api.service;

import java.util.List;

import com.iot.api.resources.Device;
import com.iot.api.resources.DeviceData;

import com.iot.api.repository.DeviceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceServiceInterface {

	@Autowired
	private  DeviceRepository repository;

	
	@Override
	public List<Device> getAllDevices() {
		return repository.findAll();
	}

	@Override
	public Device getDevice(String name) {
		// TODO Auto-generated method stub
		return repository.findByName(name);
	}

	@Override
	public void createDevice(Device dev) {
		// TODO Auto-generated method stub

	}

	@Override
	public Device deleteDeviceById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeviceData> getDeviceDataForLastHour() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeviceData> getDeviceDataForLastDay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeviceData> getDeviceDataForLastWeek() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeviceData> getDeviceDataForLastMonth() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeviceData> getDeviceDataForLastYear() {
		// TODO Auto-generated method stub
		return null;
	}

}
