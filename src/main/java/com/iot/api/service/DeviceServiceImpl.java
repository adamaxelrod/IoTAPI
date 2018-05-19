package com.iot.api.service;

import java.util.List;

import com.iot.api.resources.Device;

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
		return repository.findByName(name);
	}

	@Override
	public void createDevice(Device dev) {
		repository.insert(dev);
	}

	@Override
	public void deleteDeviceByName(String name) {
		repository.deleteBy(name);
	}
}
