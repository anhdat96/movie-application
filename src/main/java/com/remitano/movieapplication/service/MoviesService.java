package com.remitano.movieapplication.service;

import com.remitano.movieapplication.exception.CustomException;
import com.remitano.movieapplication.model.Movies;
import com.remitano.movieapplication.model.dto.MoviesDTO;
import com.remitano.movieapplication.repository.MoviesRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoviesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoviesService.class);
    @Autowired
    private MoviesRepository moviesRepository;

    public List<MoviesDTO> getAllMovies() {
        LOGGER.info("Get all movies");
        List<Movies> result = moviesRepository.findAll();
        List<MoviesDTO> response = new ArrayList<>();

        result.stream().map(movies ->
            movies.getId().toString()).collect(Collectors.toList());
        for (Movies movie: result){
            MoviesDTO moviesDTO = new MoviesDTO();
            if(movie.getId() != null){
                moviesDTO.setId(movie.getId().toString());
            }

            if(movie.getTitle() != null){
                moviesDTO.setTitle(movie.getTitle());
            }

            if(movie.getUrl() != null){
                moviesDTO.setUrl(movie.getUrl());
            }

            if(movie.getShareBy() != null){
                moviesDTO.setShareBy(movie.getShareBy());
            }

            if(movie.getDescription() != null){
                moviesDTO.setDescription(movie.getDescription());
            }

            if(!StringUtils.isEmpty(movie.getDislikeListUser())){
                moviesDTO.setDislikeListUserDTO(movie.getDislikeListUser());
            }

            if(!StringUtils.isEmpty(movie.getLikeListUser())){
                moviesDTO.setLikeListUserDTO(movie.getLikeListUser());
            }
            response.add(moviesDTO);
        }



        return response;
    }

    public MoviesDTO shareMovies(MoviesDTO moviesDTO) {
        LOGGER.info("Share movies");
        if (StringUtils.isEmpty(moviesDTO.getUrl())) {
            throw new CustomException("03");
        }
        Movies movies = new Movies();
        ObjectId objectId = new ObjectId();
        movies.setId(objectId);
        movies.setUrl(moviesDTO.getUrl());
        movies.setShareBy(moviesDTO.getShareBy());
        Movies moviesResult = moviesRepository.save(movies);

        return mapMoviesToMoviesDTO(moviesDTO, moviesResult);
    }

    private MoviesDTO mapMoviesToMoviesDTO(MoviesDTO moviesDTO, Movies moviesResult) {
        moviesDTO.setId(moviesResult.getId().toString());
        if(moviesResult.getUrl() != null){
            moviesDTO.setUrl(moviesResult.getUrl());
        }
        if(moviesResult.getShareBy() != null){
            moviesDTO.setShareBy(moviesResult.getShareBy());
        }
        return moviesDTO;

    }

    public Movies voteMovies(MoviesDTO moviesDTO) {
        LOGGER.info("Vote movies");
        ObjectId objectId = new ObjectId(moviesDTO.getId());
        Movies movies = moviesRepository.findById(objectId).get();
        if (movies == null) {
            throw new CustomException("04");
        }

        if (moviesDTO.isLikeVoted()) {
                // TH1 : like_list == null
            if (CollectionUtils.isEmpty(movies.getLikeListUser())) {
                setDataToLikeListUser(moviesDTO, movies);

                // TH2 : like_list != null
            } else if (!CollectionUtils.isEmpty(movies.getLikeListUser())){
                if (movies.getLikeListUser().contains(moviesDTO.getShareBy())) {
                    movies.getLikeListUser().remove(moviesDTO.getShareBy());
                } else {
                    movies.getLikeListUser().add(moviesDTO.getShareBy());
                }
            }
        }

        if (!moviesDTO.isLikeVoted()) {
            if (!CollectionUtils.isEmpty(movies.getLikeListUser())) {
                if (movies.getLikeListUser().contains(moviesDTO.getShareBy())) {
                    movies.getLikeListUser().remove(moviesDTO.getShareBy());
                }
            }
        }

        if (moviesDTO.isDislikeVoted()) {
                // TH1 : dislikeList == null
            if (CollectionUtils.isEmpty(movies.getDislikeListUser())) {
                setDataToDislikeListUser(moviesDTO, movies);

                // TH2 : dislikeList != null
            } else if (!CollectionUtils.isEmpty(movies.getDislikeListUser())){
                if(movies.getDislikeListUser().contains(moviesDTO.getShareBy())){
                    movies.getDislikeListUser().remove(moviesDTO.getShareBy());
                }else{
                    movies.getDislikeListUser().add(moviesDTO.getShareBy());
                }
            }

        }
        if (!moviesDTO.isDislikeVoted()) {
            if (!CollectionUtils.isEmpty(movies.getDislikeListUser())) {
                if (movies.getDislikeListUser().contains(moviesDTO.getShareBy())) {
                    movies.getDislikeListUser().remove(moviesDTO.getShareBy());
                }
            }
        }

        return moviesRepository.save(movies);
    }
    private void setDataToDislikeListUser(MoviesDTO moviesDTO, Movies movies) {
        List<String> disLikeListUser = new ArrayList<>();
        disLikeListUser.add(moviesDTO.getShareBy());
        movies.setDislikeListUser(disLikeListUser);
    }
    private void setDataToLikeListUser(MoviesDTO moviesDTO, Movies movies) {
        List<String> likeListUser = new ArrayList<>();
        likeListUser.add(moviesDTO.getShareBy());
        movies.setLikeListUser(likeListUser);
    }
}
