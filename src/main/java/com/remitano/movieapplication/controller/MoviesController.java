package com.remitano.movieapplication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/${spring.application.name}")
@RestController
public class MoviesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoviesController.class);

    // TODO: implement the controller
    // get all movies from the database

    @GetMapping(value = "/get-movies")
    public ResponseEntity<?> getAllMovies() {
        return null;
    }

    @GetMapping(value = "/share-movies")
    public ResponseEntity<?> shareMovies() {
        return null;
    }

    @GetMapping(value = "/vote-movies")
    public ResponseEntity<?> voteMovies() {
        return null;
    }


}
