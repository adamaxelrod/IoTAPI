package com.iot.api.repository;

import java.util.List;

import com.iot.api.resources.DeviceData;

import org.springframework.stereotype.Repository;

@Repository
public interface CustomMongoRepository {
	public void updateCustomDevice(DeviceData dev);
}
