package com.remitano.movieapplication.controller;

import com.remitano.movieapplication.MovieApplication;
import com.remitano.movieapplication.model.Movies;
import com.remitano.movieapplication.model.dto.MoviesDTO;
import com.remitano.movieapplication.repository.MoviesRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = MovieApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoviesControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private MoviesRepository moviesRepository;
    private String baseUrl = "http://localhost";
    private Movies avatarMovie;
    private Movies titanicMovie;
    private static RestTemplate restTemplate;
    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void beforeSetup() {
        baseUrl = baseUrl + ":" + port + "/movies";
        avatarMovie = new Movies();
        avatarMovie.setId(new ObjectId());
        avatarMovie.setShareBy("ydat14@gmail.com");
        avatarMovie.setTitle("Test object");

        titanicMovie = new Movies();
        titanicMovie.setId(new ObjectId());
        titanicMovie.setShareBy("ydat14@gmail.com");
        avatarMovie.setTitle("Test object2");

        avatarMovie = moviesRepository.save(avatarMovie);
        titanicMovie = moviesRepository.save(titanicMovie);
    }

    @AfterEach
    public void afterSetup() {
        moviesRepository.deleteAll();
    }

    @Test
    void shouldCreateMovieWhenUserShareNewMovies_test() {
        MoviesDTO avatarMovieDTO = new MoviesDTO();
        avatarMovieDTO.setTitle("Avatar");
        avatarMovieDTO.setUrl("https://www.youtube.com/watch?v=5PSNL1qE6VY");
        avatarMovieDTO.setShareBy("ydat14@gmail.com");

        Movies newMovie = restTemplate.postForObject(baseUrl + "/share-movies", avatarMovieDTO, Movies.class);
        assertNotNull(newMovie);
        assertThat(newMovie.getId()).isNotNull();
    }

    @Test
    void shouldVoteMoviesSuccessful_test() {
        MoviesDTO avatarMovieDTO = new MoviesDTO();
        avatarMovieDTO.setTitle("Avatar");
        avatarMovieDTO.setUrl("https://www.youtube.com/watch?v=5PSNL1qE6VY");
        avatarMovieDTO.setShareBy("ydat14@gmail.com");
        avatarMovieDTO.setLikeVoted(true);
        avatarMovieDTO.setId(avatarMovie.getId().toString());

        Movies newMovie = restTemplate.postForObject(baseUrl + "/vote-movies", avatarMovieDTO, Movies.class);
        assertNotNull(newMovie);
        assertThat(newMovie.getLikeListUser()).isNotNull();
    }

    @Test
    void shouldGetAllMoviesSuccessful_test() {
        List<Movies> list = restTemplate.getForObject(baseUrl + "/get-movies", List.class);
        assertThat(list.size()).isEqualTo(2);
    }
}
