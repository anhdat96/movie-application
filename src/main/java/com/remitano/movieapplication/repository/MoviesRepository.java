package com.remitano.movieapplication.repository;

import com.remitano.movieapplication.model.Movies;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesRepository extends MongoRepository<Movies, ObjectId> {
}
