package com.app.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<Application, Long>{
	public List<Application> findByAppID(String appID);
}
