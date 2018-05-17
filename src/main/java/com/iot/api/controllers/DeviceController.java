package com.iot.api.controllers;


import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import com.iot.api.resources.Device;
import com.iot.api.service.DeviceServiceInterface;

import org.json.simple.JSONObject;


@RestController
@RequestMapping("/device")
public class DeviceController {
	
	@Autowired
	private DeviceServiceInterface deviceService;
	
	//Default Logging using log4j
	public static final Log logger = LogFactory.getLog(DeviceController.class);
	
	//Incremental counter for dynamic, unique id generation
    private final AtomicLong counter = new AtomicLong();
    

    /**
     * Description: retreives deivce information based on specific device name
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
    @RequestMapping(method=RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addDevice(@RequestBody Device newDevice) {
    	
    	try {
    		if (deviceService != null) {
    			 deviceService.createDevice(counter.getAndIncrement(), newDevice);
    		}
    	}
    	catch(Exception e) {
    		//Default handling to ensure no incorrect data is returned
    		return new ResponseEntity<JSONObject>(getDefaultErrorMessage(), HttpStatus.BAD_REQUEST);
    	}	
    	    	
    	return new ResponseEntity<Device>(newDevice, HttpStatus.OK);
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
}