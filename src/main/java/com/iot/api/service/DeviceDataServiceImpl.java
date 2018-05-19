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

	/**
	 * addEntry
	 * @Description: This will add the new data point to the corresponding device
	 */
	@Override
	public void addEntry(DeviceData dev, InputDeviceInfo info) {
		//Timestamp for new data point
		Date currDate = DateUtil.getFullDate(new Date());

		if (dev == null) {			
			dev = initializeDevice(info);
			repository.save(dev);
		}
		else {
			//Create new data point object (SecondData)
			SecondData sec = new SecondData();
			sec.setSecTemperature(info.getTemperature());
			sec.setSecTimestamp(currDate);
			
			//Query to see if there is data for this minute already
			Query minQuery = new Query(Criteria.where("name").is(info.getName()).and("yearData.monthData.dayData.hourData.minData.minTimestamp").is(DateUtil.getMinDate(currDate)));			
			List<DeviceData> minQueryList = mongoTemplate.find(minQuery, DeviceData.class);
			
			//If there is no data for this minute, add a new minute object
			if (minQueryList != null && minQueryList.size() == 0) {
				//Query to see if there is data for this minute already
				Query hourQuery = new Query(Criteria.where("name").is(info.getName()).and("yearData.monthData.dayData.hourData.hourTimestamp").is(DateUtil.getHourDate(currDate)));			
				List<DeviceData> hourQueryList = mongoTemplate.find(hourQuery, DeviceData.class);
			
				MinuteData newMin = new MinuteData();
				newMin.setMinTimestamp(DateUtil.getMinDate(currDate));
				newMin.getSecData().add(sec);
				
				//If there is no data for this hour, add a new hour object (and minute, second objects as well)
				if (hourQueryList != null && hourQueryList.size() == 0) {
					//Query to see if there is data for this minute already
					Query dayQuery = new Query(Criteria.where("name").is(info.getName()).and("yearData.monthData.dayData.dayTimestamp").is(DateUtil.getDayDate(currDate)));						
					List<DeviceData> dayQueryList = mongoTemplate.find(dayQuery, DeviceData.class);
					
					HourData newHour = new HourData();
					newHour.setHourTimestamp(DateUtil.getHourDate(currDate));
					newHour.getMinData().add(newMin);
					
					//If there is no data for this day, add a new day object (and hour, minute, second objects as well)
					if (dayQueryList != null && dayQueryList.size() == 0) {
						//Query to see if there is data for this minute already
						Query monthQuery = new Query(Criteria.where("name").is(info.getName()).and("yearData.monthData.monthTimestamp").is(DateUtil.getMonthDate(currDate)));						
						List<DeviceData> monthQueryList = mongoTemplate.find(dayQuery, DeviceData.class);
						
						if (monthQueryList != null && monthQueryList.size() == 0) {
							/**
							 * TODO
							 */
						}
						else {
							DayData newDay = new DayData();
							newDay.setDayTimestamp(DateUtil.getDayDate(currDate));
							newDay.getHourData().add(newHour);
						}
					}
					//Day data exists, just add a new hour data point
					else {
						Update dayUpdate = new Update();
						dayUpdate.addToSet("yearData.monthData.0.dayData.$[].hourData", newHour);
						mongoTemplate.upsert(dayQuery, dayUpdate, DeviceData.class);	
					}
				}
				//Hour data exists, just add a new minute data point
				else {
					Update hourUpdate = new Update();
					hourUpdate.addToSet("yearData.monthData.0.dayData.0.hourData.$[].minData", newMin);
					mongoTemplate.upsert(hourQuery, hourUpdate, DeviceData.class);		
				}
			}
			//Minute data exits, just append a new second data point
			else {
				Update update = new Update();
				update.addToSet("yearData.monthData.0.dayData.0.hourData.0.minData.$[].secData", sec);
				mongoTemplate.findAndModify(minQuery, update, DeviceData.class);				
			}		 
		}
	}

	@Override
	public void deleteDeviceData(String name) {
		repository.deleteBy(name);		
	}

	@Override
	public MinuteData getDeviceDataForLastMinute(String name) {
		//Timestamp for the current minute
		Date currMinDate = DateUtil.getMinDate(new Date());
		System.out.println("CURRMINDATE: " + currMinDate.toString());
		try {
			//Query to see if there is data for this minute already for this device
			Query minQuery = new Query(Criteria.where("name").is(name).and("yearData.monthData.dayData.hourData.minData.minTimestamp").is(currMinDate));
			List<MinuteData> minQueryList = mongoTemplate.find(minQuery, MinuteData.class);
			
			if (minQueryList != null && minQueryList.size() > 0) {
				return minQueryList.get(0);
			}
			
			System.out.println("NO DATA");
		}
		catch (Exception e) {
			//TODO: replace with logger
			e.printStackTrace(System.out);
		}
		return new MinuteData();
	}
	
	@Override
	public List<DeviceData> getDeviceDataForLastHour(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeviceData> getDeviceDataForLastDay(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeviceData> getDeviceDataForLastWeek(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeviceData> getDeviceDataForLastMonth(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeviceData> getDeviceDataForLastYear(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCustomDevice(DeviceData dev) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * initializeInfo
	 * @Description: This will initialize the first entry for a device data object
	 * @param info
	 * @return
	 */
	private DeviceData initializeDevice(InputDeviceInfo info) {
		Date currDate = DateUtil.getFullDate(new Date());
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
