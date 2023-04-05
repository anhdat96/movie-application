package com.remitano.movieapplication.exception;

import lombok.Data;

@Data
public final class CustomException extends RuntimeException {

  private String customMessage;

  public CustomException(final String errorCode) {
    super(errorCode);
  }

  public CustomException(final String errorCode, final String customMessage) {
    super(errorCode);
    this.customMessage = customMessage;
  }

}
