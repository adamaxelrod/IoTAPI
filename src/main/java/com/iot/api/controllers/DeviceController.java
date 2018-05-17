package com.iot.api.controllers;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;


import com.iot.api.resources.Device;


@RestController
public class DeviceController {
	//Incremental counter for dynamic, unique id generation
    private final AtomicLong counter = new AtomicLong();

    
    @RequestMapping(method=RequestMethod.GET, path="/device")
    public Device device(@RequestParam(value="name", defaultValue="TestDevice") String name) {
        //automatically returns as JSON due to Jackson being part of the project
    	return new Device(counter.incrementAndGet(), name);
        
    }
}