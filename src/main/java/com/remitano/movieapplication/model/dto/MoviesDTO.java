package com.remitano.movieapplication.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class MoviesDTO {

    private ObjectId id;
    private String title;
    private String description;
    private String shareBy;
    private boolean likeVoted;
    private boolean dislikeVoted;
    private String url;
}
