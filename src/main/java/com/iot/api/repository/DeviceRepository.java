package com.iot.api.repository;

import java.util.List;

import com.iot.api.resources.Device;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {
	List<Device> findById(@Param("id") long id);
}