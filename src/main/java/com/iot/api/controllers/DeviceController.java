package com.iot.api.controllers;

import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseEntity;

import com.iot.api.resources.Device;


@RestController
@RequestMapping("/device")
public class DeviceController {
	
	//Default Logging using log4j
	public static final Log logger = LogFactory.getLog(DeviceController.class);
	
	//Incremental counter for dynamic, unique id generation
    private final AtomicLong counter = new AtomicLong();

    
    @RequestMapping(method=RequestMethod.GET, value="/device/{id}")
    public ResponseEntity<?> getDevice(@RequestParam(value="name", defaultValue="TestDevice") String name) {
        //automatically returns as JSON due to Jackson being part of the project
    	return new ResponseEntity<Device>(new Device(counter.incrementAndGet(), name), HttpStatus.OK);
    }
    
    
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<Device>> getAllDevices(@RequestParam(value="name", defaultValue="TestDevice") String name) {
        //automatically returns as JSON due to Jackson being part of the project
    	List<Device> devList = new ArrayList<Device>();
    	devList.add(new Device(counter.incrementAndGet(), "test1"));
    	devList.add(new Device(counter.incrementAndGet(), "test2"));
    	devList.add(new Device(counter.incrementAndGet(), "test3"));
    	
    	return new ResponseEntity<List<Device>>(devList, HttpStatus.OK);
    }
}