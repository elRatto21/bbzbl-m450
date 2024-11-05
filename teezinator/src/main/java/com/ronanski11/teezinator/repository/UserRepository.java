package com.ronanski11.teezinator.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ronanski11.teezinator.model.User;

public interface UserRepository extends MongoRepository<User, String>{

	public Optional<User> findByUsername(String username);

}
