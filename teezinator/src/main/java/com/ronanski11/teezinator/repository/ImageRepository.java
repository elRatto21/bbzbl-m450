package com.ronanski11.teezinator.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ronanski11.teezinator.model.Image;

@Repository
public interface ImageRepository extends MongoRepository<Image, String>{

}
