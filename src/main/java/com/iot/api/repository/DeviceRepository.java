package com.iot.api.repository;

import java.util.List;

import com.iot.api.resources.Device;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {
		public Device findById(@Param("id") Long id);
		public Device findByName(@Param("name") String name);
		
		@Query("{'name': ?0}")
		public Device findDeviceByName(String name);
		
		@Query(value="{name : ?0}", delete = true)
		public Long deleteBy(String name);
	
}