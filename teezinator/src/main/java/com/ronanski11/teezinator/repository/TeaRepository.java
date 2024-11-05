package com.ronanski11.teezinator.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ronanski11.teezinator.model.Tea;

public interface TeaRepository extends MongoRepository<Tea, String>{

}
