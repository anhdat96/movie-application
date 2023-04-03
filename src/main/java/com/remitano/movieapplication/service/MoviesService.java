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

@Service
public class MoviesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoviesService.class);
    @Autowired
    private MoviesRepository moviesRepository;

    public List<Movies> getAllMovies() {
        LOGGER.info("Get all movies");
        return moviesRepository.findAll();
    }

    public Movies shareMovies(MoviesDTO moviesDTO) {
        LOGGER.info("Share movies");
        if (StringUtils.isEmpty(moviesDTO.getUrl())) {
            throw new CustomException("03");
        }
        Movies movies = new Movies();
        ObjectId objectId = new ObjectId();
        movies.setId(objectId);
        movies.setUrl(moviesDTO.getUrl());
        movies.setShareBy(moviesDTO.getShareBy());
        return moviesRepository.save(movies);
    }

    public Movies voteMovies(MoviesDTO moviesDTO) {
        LOGGER.info("Vote movies");
        Movies movies = moviesRepository.findById(moviesDTO.getId()).get();
        if (movies == null) {
            throw new CustomException("04");
        }

        if (moviesDTO.isLikeVoted()) {
                // TH1 : like_list va dislikeList == null
            if (CollectionUtils.isEmpty(movies.getLikeListUser()) && CollectionUtils.isEmpty(movies.getDislikeListUser())) {
                setDataToLikeListUser(moviesDTO, movies);

                // TH2 : like_list != null ( if contain --> remove , else --> add )&& dislikeList == null
            } else if (!CollectionUtils.isEmpty(movies.getLikeListUser()) && CollectionUtils.isEmpty(movies.getDislikeListUser())) {
                removeDataFromLikeListUser(moviesDTO, movies);
                setDataToDislikeListUser(moviesDTO, movies);

                // TH3 : like_list == null && dislikeList != null
                // (2) like first time but before was dislike
            } else if (CollectionUtils.isEmpty(movies.getLikeListUser()) && !CollectionUtils.isEmpty(movies.getDislikeListUser())) {
                movies.getDislikeListUser().remove(moviesDTO.getShareBy());
                setDataToLikeListUser(moviesDTO, movies);

                // TH4 : like_list =! null && dislikeList != null
            } else if (!CollectionUtils.isEmpty(movies.getLikeListUser()) && !CollectionUtils.isEmpty(movies.getDislikeListUser())) {
                if (movies.getLikeListUser().contains(moviesDTO.getShareBy())) {
                    movies.getLikeListUser().remove(moviesDTO.getShareBy());
                    // already like then still like == dislike
                    movies.getDislikeListUser().add(moviesDTO.getShareBy());

                    // first time like but before was dislike
                } else if (!movies.getLikeListUser().contains(moviesDTO.getShareBy()) && movies.getDislikeListUser().contains(moviesDTO.getShareBy())) {
                    movies.getLikeListUser().add(moviesDTO.getShareBy());
                    movies.getDislikeListUser().remove(moviesDTO.getShareBy());
                } else {
                    // first time like
                    movies.getLikeListUser().add(moviesDTO.getShareBy());
                }
            }

        }

        if (moviesDTO.isDislikeVoted()) {
                // TH1 : like_list va dislikeList == null
            if (CollectionUtils.isEmpty(movies.getDislikeListUser()) && CollectionUtils.isEmpty(movies.getLikeListUser())) {
                setDataToDislikeListUser(moviesDTO, movies);

                // TH2 : like_list != null && dislikeList == null
            } else if (!CollectionUtils.isEmpty(movies.getLikeListUser()) && CollectionUtils.isEmpty(movies.getDislikeListUser())) {
                movies.getLikeListUser().remove(moviesDTO.getShareBy());
                setDataToDislikeListUser(moviesDTO, movies);

                // TH3 : like_list == null && dislikeList != null
            } else if (CollectionUtils.isEmpty(movies.getLikeListUser()) && !CollectionUtils.isEmpty(movies.getDislikeListUser())) {
                if (movies.getDislikeListUser().contains(moviesDTO.getShareBy())) {
                    movies.getDislikeListUser().remove(moviesDTO.getShareBy());
                    setDataToLikeListUser(moviesDTO, movies);
                } else {
                    movies.getDislikeListUser().add(moviesDTO.getShareBy());
                }

                // TH4 : like_list =! null && dislikeList != null
            } else if (!CollectionUtils.isEmpty(movies.getLikeListUser()) && !CollectionUtils.isEmpty(movies.getDislikeListUser())) {

                if (movies.getDislikeListUser().contains(moviesDTO.getShareBy())) {
                    // already dis_like then still dis_like == like
                    movies.getDislikeListUser().remove(moviesDTO.getShareBy());
                    movies.getLikeListUser().add(moviesDTO.getShareBy());

                    // dislike first time but before was like
                } else if (!movies.getDislikeListUser().contains(moviesDTO.getShareBy()) && movies.getLikeListUser().contains(moviesDTO.getShareBy())) {
                    movies.getDislikeListUser().add(moviesDTO.getShareBy());
                    movies.getLikeListUser().remove(moviesDTO.getShareBy());
                } else {
                    // first time dislike
                    movies.getDislikeListUser().add(moviesDTO.getShareBy());
                }
            }

        }

        return moviesRepository.save(movies);
    }

    private void removeDataFromLikeListUser(MoviesDTO moviesDTO, Movies movies) {
        if (movies.getLikeListUser().contains(moviesDTO.getShareBy())) {
            movies.getLikeListUser().remove(moviesDTO.getShareBy());
        } else {
            movies.getLikeListUser().add(moviesDTO.getShareBy());
        }
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
