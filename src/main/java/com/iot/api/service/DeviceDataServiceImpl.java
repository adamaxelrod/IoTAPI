package com.iot.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.iot.api.repository.DeviceDataRepository;
import com.iot.api.resources.DeviceData;
import com.iot.api.resources.MinuteData;
import com.iot.api.resources.SecondData;
import com.iot.api.resources.InputDeviceInfo;

import com.iot.api.util.DateUtil;

import org.springframework.stereotype.Service;

@Service
public class DeviceDataServiceImpl implements DeviceDataServiceInterface {

	@Autowired
	private  DeviceDataRepository repository;
	
	@Override
	public List<DeviceData> getAllDevices() {
		return repository.findAll();
	}

	@Override
	public DeviceData getDevice(String name) {
		return repository.findDeviceByName(name);
	}

	@Override
	public void addEntry(DeviceData dev, InputDeviceInfo info) {
		Date currDate = new Date();
		
		if (dev == null) {
			dev = new DeviceData();
			dev.setName(info.getName());
		}
		
 		List<SecondData> secondDataList = dev.getMin().getSecData();
 		SecondData newData = new SecondData();
 		
 		newData.setSecTemperature(info.getTemperature());
 		newData.setSecTimestamp(currDate);
 		secondDataList.add(newData);

 		MinuteData minuteData = dev.getMin();
 		minuteData.setMinTimestamp(DateUtil.getMinDate(currDate));
 		minuteData.setSecData(secondDataList);

 		dev.setMin(minuteData);
 		System.out.println("DEV NAME: " + dev.getName());
 		repository.save(dev);		
	}

	@Override
	public void deleteEntry(String name) {
		// TODO Auto-generated method stub
		
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
