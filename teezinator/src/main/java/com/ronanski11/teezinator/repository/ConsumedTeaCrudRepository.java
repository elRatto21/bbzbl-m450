package com.ronanski11.teezinator.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ronanski11.teezinator.model.ConsumedTea;

public interface ConsumedTeaCrudRepository extends MongoRepository<ConsumedTea, String>{

}
