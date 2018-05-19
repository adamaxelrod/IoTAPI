package com.iot.api.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;

import com.iot.api.repository.DeviceDataRepository;
import com.iot.api.resources.*;
import com.iot.api.util.DateUtil;


@Service
public class DeviceDataServiceImpl implements DeviceDataServiceInterface {

	@Autowired
	private  DeviceDataRepository repository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	//Default Logging using log4j
	public static final Log logger = LogFactory.getLog("iotapi");
		
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
		Date currDate = DateUtil.getFullDate(DateUtil.getCurrDate());

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
	public JSONObject getDeviceDataForLastMinute(String name) {
		//Timestamp for the current minute
		Date currMinDate = DateUtil.getMinDate(new Date());
		
		try {
			//Query to see if there is data for this minute already for this device
			Aggregation aggrQuery = Aggregation.newAggregation(Aggregation.match(Criteria.where("name").is(name)),
													Aggregation.unwind("$yearData"),
													Aggregation.unwind("$yearData.monthData"),
													Aggregation.unwind("$yearData.monthData.dayData"),
													Aggregation.unwind("$yearData.monthData.dayData.hourData"),
													Aggregation.unwind("$yearData.monthData.dayData.hourData.minData"),
												//	Aggregation.match(Criteria.where("yearData.monthData.dayData.hourData.minData.minTimestamp").is(currMinDate)),
													(Aggregation.project().and("$yearData.monthData.dayData.hourData.minData").as("minData")));

			List<Document> minQueryList = mongoTemplate.aggregate(aggrQuery, "deviceData", Document.class).getMappedResults();
			
			if (minQueryList != null && minQueryList.size() > 0) {
				JSONParser parser = new JSONParser();
				JSONObject minJson = (JSONObject) parser.parse(minQueryList.get(0).toJson());
				return minJson;
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			logger.debug(e.getMessage());
		}
		
		return null;
	}
	
	@Override
	public JSONObject getDeviceDataForLastHour(String name) {
		//Timestamp for the current minute
		Date currMinDate = DateUtil.getMinDate(new Date());

		try {
			//Query to see if there is data for this minute already for this device
			Aggregation aggrQuery = Aggregation.newAggregation(Aggregation.match(Criteria.where("name").is(name)),
					Aggregation.unwind("$yearData"),
					Aggregation.unwind("$yearData.monthData"),
					Aggregation.unwind("$yearData.monthData.dayData"),
					Aggregation.unwind("$yearData.monthData.dayData.hourData"),
					//	Aggregation.match(Criteria.where("yearData.monthData.dayData.hourData.minData.minTimestamp").is(currMinDate)),
					(Aggregation.project().and("$yearData.monthData.dayData.hourData").as("hourData")));

			List<Document> hourQueryList = mongoTemplate.aggregate(aggrQuery, "deviceData", Document.class).getMappedResults();

			if (hourQueryList != null && hourQueryList.size() > 0) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(hourQueryList.get(0).toJson());
				return json;
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			logger.debug(e.getMessage());
		}

		return null;
	}

	@Override
	public JSONObject getDeviceDataForLastDay(String name) {
		//Timestamp for the current minute
		Date currMinDate = DateUtil.getMinDate(new Date());

		try {
			//Query to see if there is data for this minute already for this device
			Aggregation aggrQuery = Aggregation.newAggregation(Aggregation.match(Criteria.where("name").is(name)),
					Aggregation.unwind("$yearData"),
					Aggregation.unwind("$yearData.monthData"),
					Aggregation.unwind("$yearData.monthData.dayData"),
					//	Aggregation.match(Criteria.where("yearData.monthData.dayData.hourData.minData.minTimestamp").is(currMinDate)),
					(Aggregation.project().and("$yearData.monthData.dayData").as("dayData")));

			List<Document> dayQueryList = mongoTemplate.aggregate(aggrQuery, "deviceData", Document.class).getMappedResults();

			if (dayQueryList != null && dayQueryList.size() > 0) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(dayQueryList.get(0).toJson());
				return json;
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			logger.debug(e.getMessage());
		}

		return null;
	}

	@Override
	public JSONObject getDeviceDataForLastWeek(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getDeviceDataForLastMonth(String name) {
		//Timestamp for the current minute
		Date currMinDate = DateUtil.getMinDate(new Date());

		try {
			//Query to see if there is data for this minute already for this device
			Aggregation aggrQuery = Aggregation.newAggregation(Aggregation.match(Criteria.where("name").is(name)),
					Aggregation.unwind("$yearData"),
					Aggregation.unwind("$yearData.monthData"),
					//	Aggregation.match(Criteria.where("yearData.monthData.dayData.hourData.minData.minTimestamp").is(currMinDate)),
					(Aggregation.project().and("$yearData.monthData").as("monthData")));

			List<Document> monthQueryList = mongoTemplate.aggregate(aggrQuery, "deviceData", Document.class).getMappedResults();

			if (monthQueryList != null && monthQueryList.size() > 0) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(monthQueryList.get(0).toJson());
				return json;
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			logger.debug(e.getMessage());
		}

		return null;
	}

	@Override
	public JSONObject getDeviceDataForLastYear(String name) {
		//Timestamp for the current minute
		Date currMinDate = DateUtil.getMinDate(new Date());

		try {
			//Query to see if there is data for this minute already for this device
			Aggregation aggrQuery = Aggregation.newAggregation(Aggregation.match(Criteria.where("name").is(name)),
					Aggregation.unwind("$yearData"),
					//	Aggregation.match(Criteria.where("yearData.monthData.dayData.hourData.minData.minTimestamp").is(currMinDate)),
					Aggregation.project("yearData"));

			List<Document> yearQueryList = mongoTemplate.aggregate(aggrQuery, "deviceData", Document.class).getMappedResults();

			if (yearQueryList != null && yearQueryList.size() > 0) {
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(yearQueryList.get(0).toJson());
				return json;
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			logger.debug(e.getMessage());
		}

		return null;
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
