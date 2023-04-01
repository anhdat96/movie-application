package com.remitano.movieapplication.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class ErrorMessage {

    private Integer code;
    private List<String> messages;
}
