package com.remitano.movieapplication;

import com.remitano.movieapplication.repository.MoviesRepository;
import com.remitano.movieapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class MovieApplication {

	@Autowired
	MoviesRepository moviesRepository;

	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(MovieApplication.class, args);
	}

}
