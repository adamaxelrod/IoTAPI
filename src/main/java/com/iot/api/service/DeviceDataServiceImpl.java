package com.iot.api.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.json.simple.JSONArray;
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

import com.google.gson.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class DeviceDataServiceImpl implements DeviceDataServiceInterface {

	@Autowired
	private  DeviceDataRepository repository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	//Default Logging using log4j
	public static final Logger logger = LoggerFactory.getLogger(DeviceDataServiceImpl.class);
		
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
	public JSONArray getDeviceDataForLastMinute(String name) {
		//Timestamp for the current minute
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -1);
		Date minuteAgo = cal.getTime();
		JSONArray arr = new JSONArray();
		
		try {	
			//Query to see if there is data for this minute already for this device
			Aggregation aggrQuery = Aggregation.newAggregation(Aggregation.match(Criteria.where("name").is(name)),
													Aggregation.unwind("$yearData"),
													Aggregation.unwind("$yearData.monthData"),
													Aggregation.unwind("$yearData.monthData.dayData"),
													Aggregation.unwind("$yearData.monthData.dayData.hourData"),
													Aggregation.unwind("$yearData.monthData.dayData.hourData.minData"),
													(Aggregation.project().and("$yearData.monthData.dayData.hourData.minData").as("minData")
															.and("yearData.monthData.dayData.hourData.minData.minTimestamp").gte(minuteAgo)));

			List<Document> minQueryList = mongoTemplate.aggregate(aggrQuery, "deviceData", Document.class).getMappedResults();
			arr = this.convertDocListToArray(minQueryList, "minute", name);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return arr;
	}
	
	@Override
	public JSONArray getDeviceDataForLastHour(String name) {
		//Timestamp for date a minute ago	
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -1);
		Date hourAgo = cal.getTime();
		JSONArray arr = new JSONArray();
		
		try {
			//Query to see if there is data for this minute already for this device
			Aggregation aggrQuery = Aggregation.newAggregation(Aggregation.match(Criteria.where("name").is(name)),
					Aggregation.unwind("$yearData"),
					Aggregation.unwind("$yearData.monthData"),
					Aggregation.unwind("$yearData.monthData.dayData"),
					Aggregation.unwind("$yearData.monthData.dayData.hourData"),
					Aggregation.match(Criteria.where("yearData.monthData.dayData.hourData.hourTimestamp").gte(hourAgo)),
					(Aggregation.project().and("$yearData.monthData.dayData.hourData").as("hourData")));

			List<Document> hourQueryList = mongoTemplate.aggregate(aggrQuery, "deviceData", Document.class).getMappedResults();

			arr = this.convertDocListToArray(hourQueryList, "hour", name);
		}
		catch (Exception e) {			
			logger.error(e.getMessage(), e);
		}

		return arr;
	}

	@Override
	public JSONArray getDeviceDataForLastDay(String name) {
		//Timestamp for date a week ago		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date dayAgo = cal.getTime(); 
		
		JSONArray arr = new JSONArray();
		
		try {
			//Query to see if there is data for this minute already for this device
			Aggregation aggrQuery = Aggregation.newAggregation(Aggregation.match(Criteria.where("name").is(name)),
					Aggregation.unwind("$yearData"),
					Aggregation.unwind("$yearData.monthData"),
					Aggregation.unwind("$yearData.monthData.dayData"),
					Aggregation.match(Criteria.where("yearData.monthData.dayData.dayTimestamp").gte(dayAgo)),
					(Aggregation.project().and("$yearData.monthData.dayData").as("dayData")));

			List<Document> dayQueryList = mongoTemplate.aggregate(aggrQuery, "deviceData", Document.class).getMappedResults();
			arr = this.convertDocListToArray(dayQueryList, "day", name);			
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return arr;
	}

	@Override
	public JSONArray getDeviceDataForLastWeek(String name) {
		//Timestamp for date a week ago		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		Date weekAgo = cal.getTime(); 
		
		JSONArray arr = new JSONArray();
		
		try {
			//Query to see if there is data for the last week range
			Aggregation aggrQuery = Aggregation.newAggregation(Aggregation.match(Criteria.where("name").is(name)),
					Aggregation.unwind("$yearData"),
					Aggregation.unwind("$yearData.monthData"),
					Aggregation.unwind("$yearData.monthData.dayData"),
					Aggregation.match(Criteria.where("yearData.monthData.dayData.dayTimestamp").gte(weekAgo)),
					(Aggregation.project().and("$yearData.monthData.dayData").as("dayData")));

			List<Document> dayQueryList = mongoTemplate.aggregate(aggrQuery, "deviceData", Document.class).getMappedResults();
			arr = this.convertDocListToArray(dayQueryList, "week", name);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return arr;
	}

	@Override
	public JSONArray getDeviceDataForLastMonth(String name) {
		//Date corresponding to one year ago
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date monthAgo = cal.getTime(); 
		
		JSONArray arr = new JSONArray();

		try {
			//Query to see if there is data for this minute already for this device
			Aggregation aggrQuery = Aggregation.newAggregation(Aggregation.match(Criteria.where("name").is(name)),
					Aggregation.unwind("$yearData"),
					Aggregation.unwind("$yearData.monthData"),
					Aggregation.match(Criteria.where("yearData.monthData.monthTimestamp").gte(monthAgo)),
					(Aggregation.project().and("$yearData.monthData").as("monthData")));

			List<Document> monthQueryList = mongoTemplate.aggregate(aggrQuery, "deviceData", Document.class).getMappedResults();
			arr = this.convertDocListToArray(monthQueryList, "month", name);				
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return arr;
	}

	@Override
	public JSONArray getDeviceDataForLastYear(String name) {
		//Date corresponding to one year ago
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		Date yearAgo = cal.getTime(); 

		JSONArray arr = new JSONArray();
		
		try {
			//Query to see if there is data for this minute already for this device
			Aggregation aggrQuery = Aggregation.newAggregation(Aggregation.match(Criteria.where("name").is(name)),
					Aggregation.unwind("$yearData"),
					Aggregation.match(Criteria.where("yearData.yearTimestamp").gte(yearAgo)),
					Aggregation.project("yearData"));

			List<Document> yearQueryList = mongoTemplate.aggregate(aggrQuery, "deviceData", Document.class).getMappedResults();
			arr = this.convertDocListToArray(yearQueryList, "year", name);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return arr;
	}

	/**
	 * convertDocListToArray
	 * @Description: returns a JSONArray representation of a list of MongoDB documents fetched from an aggregation query
	 * @param list
	 * @param type
	 * @param name
	 * @return
	 */
	private JSONArray convertDocListToArray(List<Document> list, String type, String name) {
		JSONArray arr = new JSONArray();
		
		try {
			if (list != null && list.size() > 0) {
				JSONParser parser = new JSONParser();
				
				for (Document doc : list) {				
					JSONObject docObj = (JSONObject)parser.parse(doc.toJson());		
					arr.add(docObj);					
				}				
			}
			else {
				arr.add(new JSONObject().put("message", new String("No data available for the last " + type + " for device: " + name)));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return arr;
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
		yd.setYearTimestamp(DateUtil.getYearDate(currDate));

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
