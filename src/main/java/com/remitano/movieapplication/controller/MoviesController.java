package com.remitano.movieapplication.controller;

import com.remitano.movieapplication.model.Movies;
import com.remitano.movieapplication.model.dto.MoviesDTO;
import com.remitano.movieapplication.service.MoviesService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/${spring.application.name}")
@RestController
public class MoviesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoviesController.class);
    @Autowired
    private MoviesService moviesService;

    @GetMapping(value = "/get-movies")
    public ResponseEntity<List<Movies>> getAllMovies() {
        LOGGER.info("Request to get all movies");
        return new ResponseEntity<>(moviesService.getAllMovies(), HttpStatus.OK);
    }

    @PostMapping(value = "/share-movies")
    public ResponseEntity<Movies> shareMovies(@RequestBody @Valid MoviesDTO moviesDTO) {
        LOGGER.info("Request to share a movie");
        return new ResponseEntity<>(moviesService.shareMovies(moviesDTO), HttpStatus.OK);
    }

    @PostMapping(value = "/vote-movies")
    public ResponseEntity<Movies> voteMovies(@RequestBody @Valid MoviesDTO moviesDTO) {
        LOGGER.info("Request to vote or un vote a movie");
        return new ResponseEntity<>(moviesService.voteMovies(moviesDTO), HttpStatus.OK);
    }


}
