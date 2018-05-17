package com.iot.api.repository;

import java.util.List;

import com.iot.api.resources.Device;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {
		Device findById(@Param("id") Long id);
		Device findByName(@Param("name") String name);

}