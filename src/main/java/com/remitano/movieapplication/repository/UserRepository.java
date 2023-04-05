package com.remitano.movieapplication.repository;

import com.remitano.movieapplication.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}