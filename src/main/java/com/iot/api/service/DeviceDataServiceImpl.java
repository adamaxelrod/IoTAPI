package com.iot.api.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.*;

import com.iot.api.repository.DeviceDataRepository;
import com.iot.api.repository.CustomMongoRepository;

import com.iot.api.resources.*;

import com.iot.api.util.DateUtil;
import com.mongodb.client.result.UpdateResult;

import org.springframework.stereotype.Service;

@Service
public class DeviceDataServiceImpl implements DeviceDataServiceInterface, CustomMongoRepository {

	@Autowired
	private  DeviceDataRepository repository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
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
			dev = initializeDevice(info);
			repository.save(dev);
		}
		else {
			SecondData sec = new SecondData();
			sec.setSecTemperature(info.getTemperature());
			sec.setSecTimestamp(currDate);
			
			 for (MonthData month : dev.getYearData().getMonthData()) {
			        for (DayData day : month.getDayData()) {
			        	for (HourData hour : day.getHourData()) {
			        		for (MinuteData min : hour.getMinData()) {
			        			System.out.println("FOUND MIN: " + min.getMinTimestamp());
			        			System.out.println("ACTUAL MIN: " + DateUtil.getMinDate(currDate));
			        			
			        			Query sQuery = new Query();
					            Criteria sCriteria = Criteria.where("yearData.$.minTimestamp").is((DateUtil.getMinDate(currDate)));
					            
					            Update secUpdate = new Update();
					            secUpdate.push("secData", sec);
					            
					            UpdateResult sUpdateResult = mongoTemplate.updateFirst(sQuery, secUpdate, DeviceData.class);
					            
					            if (sUpdateResult.getModifiedCount() == 0) {
					            	System.out.println("MODIFIED 0");
					                Query pQuery = new Query();
					                Criteria pCriteria = Criteria.where("yearData.$.minTimestamp").is((DateUtil.getMinDate(currDate)));
					                pQuery.addCriteria(pCriteria);
					                Update pUpdate = new Update();
					                pUpdate.push("yearData.$.secData", sec);
					                mongoTemplate.updateFirst(pQuery, pUpdate, DeviceData.class);
					            }
			        		}
			        	}		        
			        }
			    }
			 
			Query query = new Query(Criteria.where("name").is(info.getName()));
			Update update = new Update();
			update.push("yearData.monthData.dayData.hourData.minData.secData", sec);
			
			mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().upsert(true), DeviceData.class);
		}
 		//repository.save(dev);		
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

	@Override
	public void updateCustomDevice(DeviceData dev) {
		// TODO Auto-generated method stub
		
	}

	
	private DeviceData initializeDevice(InputDeviceInfo info) {
		Date currDate = new Date();
		DeviceData dev = new DeviceData();
		dev.setName(info.getName());
		
		YearData yd = new YearData();
		yd.setYearTimestamp(currDate);

		MonthData mod = new MonthData();
		mod.setMonthTimestamp(DateUtil.getMonthDate(currDate));
		
		DayData dd = new DayData();
		dd.setDayTimestamp(DateUtil.getDayDate(currDate));
		
		HourData hd = new HourData();
		hd.setHourTimestamp(DateUtil.getHourDate(currDate));
		
		MinuteData mid = new MinuteData();
		mid.setMinTimestamp(DateUtil.getMinDate(currDate));
		
		SecondData sec = new SecondData();
		sec.setSecTemperature(info.getTemperature());
		sec.setSecTimestamp(currDate);
		
		mid.getSecData().add(sec);
		hd.getMinData().add(mid);
		dd.getHourData().add(hd);
		mod.getDayData().add(dd);
		yd.getMonthData().add(mod);
		
		dev.setYearData(yd);
		
		return dev;
	}
}
