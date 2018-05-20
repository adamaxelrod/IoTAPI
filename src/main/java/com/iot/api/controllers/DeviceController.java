package com.iot.api.controllers;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import com.iot.api.resources.Device;
import com.iot.api.resources.DeviceData;

import com.iot.api.resources.InputDeviceInfo;

import com.iot.api.service.DeviceServiceInterface;
import com.iot.api.service.DeviceDataServiceInterface;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


@RestController
@RequestMapping("/device")
public class DeviceController {
	
	@Autowired
	private DeviceServiceInterface deviceService;
	
	@Autowired
	private DeviceDataServiceInterface dataService;
	
	//Default Logging using log4j2
	public static final Logger logger = LoggerFactory.getLogger(DeviceController.class);
	

    /**
     * Description: retrieves device information based on specific device name
     * @GET /device/{name}/
     */
    @RequestMapping(method=RequestMethod.GET, value="/{name}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDeviceByName(@PathVariable("name")  String name) {
    	Device dev = null;
    	try {
    		if (deviceService != null) {
    			dev = deviceService.getDevice(name);
    		}
    	}
    	catch(Exception e) {
    		//Default handling to ensure no incorrect data is returned
    		logger.error(e.getMessage(), e);
    		dev = null;
    	}

    	//Handle service/database down or other error scenarios
    	if (dev == null) {    	    		
    		return new ResponseEntity<JSONObject>(getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}
    	
    	return new ResponseEntity<Device>(deviceService.getDevice(name), HttpStatus.OK);
    }
    
    
    /**
     * @Description: retrieves device configuration information for all devices 
     * @GET /device
     */
    @RequestMapping(method=RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Device>> getAllDevices(@RequestParam(value="name", defaultValue="TestDevice") String name) {
    	List<Device> devList = null;
    	
    	try {
    		if (deviceService != null) {
    			devList = deviceService.getAllDevices();
    		}
    	}
    	catch(Exception e) {
    		//Default handling to ensure no incorrect data is returned
    		logger.error(e.getMessage(), e);
    		devList = null;
    	}	
    	
    	if (devList == null) {    	    		
    		return new ResponseEntity<List<Device>>(devList, HttpStatus.BAD_REQUEST);
    	}
    	
    	return new ResponseEntity<List<Device>>(devList, HttpStatus.OK);
    }
    
    
    /**
     * @Description: retrieves device configuration information for all devices 
     * @POST /device
     */
    @RequestMapping(method=RequestMethod.POST,  produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addDevice(@RequestBody Device newDevice) {
    	
    	try {
    		if (deviceService != null) {
    			 deviceService.createDevice(newDevice);
    		}
    	}
    	catch(Exception e) {
    		//Default handling to ensure no incorrect data is returned
    		logger.error(e.getMessage(), e);
    		return new ResponseEntity<JSONObject>(getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}	
    	    	
    	return new ResponseEntity<Device>(newDevice, HttpStatus.OK);
    }
    
    
    /**
     * @Description: retrieves device configuration information for all devices 
     * @DELETE /device
     */
    @RequestMapping(method=RequestMethod.DELETE, value="/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDevice(@PathVariable("name") String name) {
    	
    	try {
    		if (deviceService != null) {
    			 deviceService.deleteDeviceByName(name);
    		}
    	}
    	catch(Exception e) {
    		//Default handling to ensure no incorrect data is returned
    		logger.error(e.getMessage(), e);
    		return new ResponseEntity<JSONObject>(getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}	
    	    	
    	return new ResponseEntity<JSONObject>(getDefaultSuccessMessage(), HttpStatus.OK);
    }
    
    
    /**
     * @Description: retrieves device data information for all devices 
     * @GET /device/data
     */
    @RequestMapping(method=RequestMethod.GET,  value="/data", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeviceData>> getAllDeviceData(@RequestParam(value="name", defaultValue="TestDevice") String name) {
    	List<DeviceData> devList = null;
    	
    	try {
    		if (dataService != null) {
    			devList = dataService.getAllDevices();
    		}
    	}
    	catch(Exception e) {
    		//Default handling to ensure no incorrect data is returned
    		logger.error(e.getMessage(), e);
    		devList = null;
    	}	
    	
    	if (devList == null) {    	    		
    		return new ResponseEntity<List<DeviceData>>(devList, HttpStatus.BAD_REQUEST);
    	}
    	
    	return new ResponseEntity<List<DeviceData>>(devList, HttpStatus.OK);
    }
    
    
    /**
     * @Description: retrieves device data information for a specific device
     * @GET /device/data/{name}
     */
    @RequestMapping(method=RequestMethod.GET, value="/data/{name}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDeviceDataByName(@PathVariable(value="name") String name) {
    	DeviceData dev = null;
    	
    	try {
    		if (dataService != null) {
    			dev = dataService.getDevice(name);
    		}
    	}
    	catch(Exception e) {
    		//Default handling to ensure no incorrect data is returned
    		logger.error(e.getMessage(), e);
    		return new ResponseEntity<JSONObject>(this.getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}	
    	
    	//Handle service/database down or other error scenarios
    	if (dev == null) {    	    		
    		return new ResponseEntity<JSONObject>(this.getDefaultJSONObject(), HttpStatus.OK);
    	}    	
    	
    	return new ResponseEntity<DeviceData>(dev, HttpStatus.OK);
    }
    
    
    /**
     * @Description: retrieves device data information for a specific device
     * @GET /device/data/{name}
     */
    @RequestMapping(method=RequestMethod.GET, value="/data/{name}/minute", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLastMinuteData(@PathVariable(value="name") String name) {
    	logger.debug("Requesting minute data for: " + name);
    	
    	JSONArray minData = null;

    	try {
    		if (dataService != null) {
    			minData = dataService.getDeviceDataForLastMinute(name);
    		}
    	}
    	catch(Exception e) {
    		//Default handling to ensure no incorrect data is returned
    		logger.error(e.getMessage(), e);
    		return new ResponseEntity<JSONObject>(this.getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}	
    	
    	//Handle service/database down or other error scenarios
    	if (minData == null) {    	    		
    		return new ResponseEntity<JSONObject>(this.getDefaultJSONObject(), HttpStatus.OK);
    	}    	
    	
    	return new ResponseEntity<JSONArray>(minData, HttpStatus.OK);
    }
    
    
    /**
     * @Description: retrieves device data information for a specific device
     * @GET /device/data/{name}
     */
    @RequestMapping(method=RequestMethod.GET, value="/data/{name}/hour", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLastHourData(@PathVariable(value="name") String name) {
    	JSONArray hourData = null;

    	try {
    		if (dataService != null) {
    			hourData = dataService.getDeviceDataForLastHour(name);
    		}
    	}
    	catch(Exception e) {
    		//Default handling to ensure no incorrect data is returned
    		logger.error(e.getMessage(), e);
    		return new ResponseEntity<JSONObject>(this.getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}	
    	
    	//Handle service/database down or other error scenarios
    	if (hourData == null) {    	    		
    		return new ResponseEntity<JSONObject>(this.getDefaultJSONObject(), HttpStatus.OK);
    	}    	
    	
    	return new ResponseEntity<JSONArray>(hourData, HttpStatus.OK);
    }
    
    
    /**
     * @Description: retrieves device data information for a specific device
     * @GET /device/data/{name}
     */
    @RequestMapping(method=RequestMethod.GET, value="/data/{name}/day", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLastDayData(@PathVariable(value="name") String name) {
    	JSONArray dayData = null;

    	try {
    		if (dataService != null) {
    			dayData = dataService.getDeviceDataForLastDay(name);
    		}
    	}
    	catch(Exception e) {
    		//Default handling to ensure no incorrect data is returned
    		logger.error(e.getMessage(), e);
    		return new ResponseEntity<JSONObject>(this.getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}	
    	
    	//Handle service/database down or other error scenarios
    	if (dayData == null) {    	    		
    		return new ResponseEntity<JSONObject>(this.getDefaultJSONObject(), HttpStatus.OK);
    	}    	
    	
    	return new ResponseEntity<JSONArray>(dayData, HttpStatus.OK);
    }
    
    
    /**
     * @Description: retrieves device data information for a specific device
     * @GET /device/data/{name}
     */
    @RequestMapping(method=RequestMethod.GET, value="/data/{name}/week", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLastWeekData(@PathVariable(value="name") String name) {
    	JSONArray weekData = null;

    	try {
    		if (dataService != null) {
    			weekData = dataService.getDeviceDataForLastWeek(name);
    		}
    	}
    	catch(Exception e) {
    		//Default handling to ensure no incorrect data is returned
    		logger.error(e.getMessage(), e);
    		return new ResponseEntity<JSONObject>(this.getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}	
    	
    	//Handle service/database down or other error scenarios
    	if (weekData == null) {    	    		
    		return new ResponseEntity<JSONObject>(this.getDefaultJSONObject(), HttpStatus.OK);
    	}    	
    	
    	return new ResponseEntity<JSONArray>(weekData, HttpStatus.OK);
    }
    
    /**
     * @Description: retrieves device data information for a specific device
     * @GET /device/data/{name}
     */
    @RequestMapping(method=RequestMethod.GET, value="/data/{name}/month", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLastMonthData(@PathVariable(value="name") String name) {
    	JSONArray monthData = null;

    	try {
    		if (dataService != null) {
    			monthData = dataService.getDeviceDataForLastMonth(name);
    		}
    	}
    	catch(Exception e) {
    		//Default handling to ensure no incorrect data is returned
    		logger.error(e.getMessage(), e);
    		return new ResponseEntity<JSONObject>(this.getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}	
    	
    	//Handle service/database down or other error scenarios
    	if (monthData == null) {    	    		
    		return new ResponseEntity<JSONObject>(this.getDefaultJSONObject(), HttpStatus.OK);
    	}    	
    	
    	return new ResponseEntity<JSONArray>(monthData, HttpStatus.OK);
    }
    
    /**
     * @Description: retrieves device data information for a specific device
     * @GET /device/data/{name}
     */
    @RequestMapping(method=RequestMethod.GET, value="/data/{name}/year", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLastYearData(@PathVariable(value="name") String name) {
    	JSONArray yearData = null;

    	try {
    		if (dataService != null) {
    			yearData = dataService.getDeviceDataForLastYear(name);
    		}
    	}
    	catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		return new ResponseEntity<JSONObject>(this.getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}	
    	
    	//Handle service/database down or other error scenarios
    	if (yearData == null) {    	    		
    		return new ResponseEntity<JSONObject>(this.getDefaultJSONObject(), HttpStatus.OK);
    	}    	
    	
    	return new ResponseEntity<JSONArray>(yearData, HttpStatus.OK);
    }
    
    
    /**
     * @Description: retrieves device configuration information for all devices 
     * @POST /device/data
     * RequestBody example:  {name: "TEST1", temperature: 50.5}
     */
    @RequestMapping(method=RequestMethod.POST,  value="/data", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addDeviceData(@RequestBody InputDeviceInfo newData) {
    	DeviceData data = null;
    	
    	try {    		    		
    		data = dataService.getDevice(newData.getName());
    		
    		if (dataService != null) {
    			 dataService.addEntry(data, newData);
    		}
    	}
    	catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		//Default handling to ensure no incorrect data is returned
    		return new ResponseEntity<JSONObject>(getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}	
    	    	
    	return new ResponseEntity<DeviceData>(data, HttpStatus.OK);
    }
    
    
    /**
     * @Description: Deletes device configuration information for a specific device 
     * @DELETE /device
     */
    @RequestMapping(method=RequestMethod.DELETE, value="/data/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDeviceData(@PathVariable("name") String name) {
    	
    	try {
    		if (dataService != null) {
    			 dataService.deleteDeviceData(name);
    		}
    	}
    	catch(Exception e) {
    		logger.error(e.getMessage(), e);
    		return new ResponseEntity<JSONObject>(getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}	
    	    	
    	return new ResponseEntity<JSONObject>(getDefaultSuccessMessage(), HttpStatus.OK);
    }
    
    
    /**
     * @Description: returns a basic JSON response to indicate a general error occurred
     */
    private JSONObject getDefaultSuccessMessage() {
    	JSONObject resp = new JSONObject();
		resp.put("message", "Successfully deleted");
		
		return resp;
    }
    
    
    /**
     * @Description: returns a basic JSON response to indicate a general error occurred
     */
    private JSONObject getDefaultErrorMessage() {
    	JSONObject resp = new JSONObject();
		resp.put("errorCode", 1000);
		resp.put("message", "Service is not reachable");
		
		return resp;
    }
    
    
    /**
     * @Description: returns a basic JSON response to indicate a general error occurred
     */
    private List<JSONObject> getDefaultErrorMessageList() {
    	List<JSONObject> errList = new ArrayList<JSONObject>();
    	
    	JSONObject resp = new JSONObject();
		resp.put("errorCode", 1000);
		resp.put("message", "Service is not reachable");
		
		errList.add(resp);
		
		return errList;
    }
    
    

    /**
     * @Description: returns a basic JSON response to indicate a general error occurred
     */
    private JSONObject getDefaultJSONObject() {
    	JSONObject resp = new JSONObject();
		resp.put("message", "No Data for the requested device");
		
		return resp;
    }
}