package com.remitano.movieapplication.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Document(collection = "movie")
public class Movies {

    @Id
    private ObjectId id;
    private String title;
    private String description;
    private String shareBy;
    private String url;
    private List<String> likeListUser;
    private List<String> dislikeListUser;

}
