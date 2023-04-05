package com.remitano.movieapplication.repository;

import com.remitano.movieapplication.model.Role;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, ObjectId> {
}
