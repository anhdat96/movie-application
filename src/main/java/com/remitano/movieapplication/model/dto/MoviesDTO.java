package com.remitano.movieapplication.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class MoviesDTO {
    private String id;
    private String title;
    private String description;
    private String shareBy;
    private boolean likeVoted;
    private boolean dislikeVoted;
    private String url;
    private List<String> likeListUserDTO;
    private List<String> dislikeListUserDTO;
}
