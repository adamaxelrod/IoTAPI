package com.iot.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.iot.api"})
public class IoTapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IoTapiApplication.class, args);
	}
}
