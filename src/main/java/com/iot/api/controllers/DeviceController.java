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
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import com.iot.api.resources.Device;
import com.iot.api.service.DeviceServiceInterface;


@RestController
@RequestMapping("/device")
public class DeviceController {
	
	@Autowired
	private DeviceServiceInterface deviceService;
	
	
	//Default Logging using log4j
	public static final Log logger = LogFactory.getLog(DeviceController.class);
	
	//Incremental counter for dynamic, unique id generation
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(method=RequestMethod.GET, value="/{name}")
    public ResponseEntity<?> getDeviceByName(@PathVariable("name")  String name) {
    	return new ResponseEntity<Device>(deviceService.getDevice(name), HttpStatus.OK);
    }
    
    
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<Device>> getAllDevices(@RequestParam(value="name", defaultValue="TestDevice") String name) {
    	List<Device> devList = deviceService.getAllDevices();
    	
    	//automatically returns as JSON due to Jackson being part of the project
   /* 	List<Device> devList = new ArrayList<Device>();
    	devList.add(new Device(counter.incrementAndGet(), "test1"));
    	devList.add(new Device(counter.incrementAndGet(), "test2"));
    	devList.add(new Device(counter.incrementAndGet(), "test3"));
    */	
    	return new ResponseEntity<List<Device>>(devList, HttpStatus.OK);
    }
}