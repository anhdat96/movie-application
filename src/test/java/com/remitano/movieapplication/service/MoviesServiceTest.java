package com.remitano.movieapplication.service;

import com.remitano.movieapplication.model.Movies;
import com.remitano.movieapplication.model.User;
import com.remitano.movieapplication.model.dto.MoviesDTO;
import com.remitano.movieapplication.repository.MoviesRepository;
import com.remitano.movieapplication.repository.RoleRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MoviesServiceTest {
    @Mock
    private MoviesRepository moviesRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private MoviesService moviesService ;

    @BeforeEach
    public void beforeSetup() {
        Movies movie = new Movies();
        movie.setId(new ObjectId("5afea3b5bc7f8d04fc61d525"));
        movie.setShareBy("ydat12@gmail.com");
        moviesRepository.save(movie);
    }

    @DisplayName("Test getAllMovies() method")
    @Test
    void shouldGetAllMoviesSuccessful() {
        List<Movies> moviesList = new ArrayList<>();
        Movies movie = new Movies();
        movie.setId(new ObjectId());
        movie.setTitle("testing movie");
        movie.setUrl("https://www.youtube.com/watch?v=6ZfuNTqbHE8");

        when(moviesRepository.findAll()).thenReturn(moviesList);

        assertNotNull(moviesService.getAllMovies());
    }

    @DisplayName("Test shareMovies() method")
    @Test
    void shouldShareMoviesSuccessful() {
        MoviesDTO moviesDTO = new MoviesDTO();
        moviesDTO.setId(String.valueOf(new ObjectId()));
        moviesDTO.setTitle("testing movie");
        moviesDTO.setUrl("https://www.youtube.com/watch?v=6ZfuNTqbHE8");

        Movies movie = new Movies();
        movie.setId(new ObjectId());
        movie.setTitle("testing movie");
        movie.setUrl("https://www.youtube.com/watch?v=6ZfuNTqbHE8");

        when(moviesRepository.save(any())).thenReturn(movie);
        MoviesDTO result = moviesService.shareMovies(moviesDTO);

        assertNotNull(moviesService.getAllMovies());
        assertEquals(movie.getId().toString(),result.getId());
    }
}
