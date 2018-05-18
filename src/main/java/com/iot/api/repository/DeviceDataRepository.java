package com.iot.api.repository;

import java.util.List;

import com.iot.api.resources.DeviceData;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceDataRepository extends MongoRepository<DeviceData, String> {
		public DeviceData findByName(@Param("name") String name);
		
		@Query("{'name': ?0}")
		public DeviceData findDeviceByName(String name);

	
}