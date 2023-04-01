package com.remitano.movieapplication.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PropertySource("classpath:error.properties")
@RestControllerAdvice
public final class CustomExceptionHandler  extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);
    private static final int INTERNAL_SERVER_ERROR_CODE = 9999;
    private static final String INTERNAL_SERVER_ERROR_MSG = "Internal Server Error";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private Environment env;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorMessage> handleCustomException(
            final CustomException customException,
            final WebRequest request
    ) {
        LOGGER.error("CustomException: " + customException);
        Integer httpCode = null;
        if (Strings.isNotBlank(customException.getCustomMessage())) {
            try {
                httpCode = Integer.parseInt(customException.getMessage());
            } catch (final Exception ex) {
                httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            }
            return new ResponseEntity<>(
                    new ErrorMessage(
                            httpCode,
                            Collections.singletonList(customException.getCustomMessage())),
                    HttpStatus.valueOf(httpCode)
            );
        }

        ErrorMessage errorDetails;
        List<String> errorList = new ArrayList<>();
        try {
            final String errorMsg = getErrorDescription(
                    getConfigValue(customException.getMessage()));
            LOGGER.info("errorMsg {}", errorMsg);
            httpCode = getHttpCode(getConfigValue(customException.getMessage()));

            errorList.add(null != errorMsg ? errorMsg : INTERNAL_SERVER_ERROR_MSG);
            errorDetails = new ErrorMessage(Integer.parseInt(customException.getMessage()), errorList);

        } catch (final Exception ex) {
            LOGGER.error("Unable to process error message {}", customException, ex);
            errorList.add(INTERNAL_SERVER_ERROR_MSG);
            errorDetails = new ErrorMessage(INTERNAL_SERVER_ERROR_CODE, errorList);
        }

        return new ResponseEntity<>(errorDetails, null != httpCode ? HttpStatus.valueOf(httpCode) : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getErrorDescription(final String errorRow) {
        try {
            return OBJECT_MAPPER.readTree(errorRow).get("errorDescription").asText();
        } catch (final JsonProcessingException ex) {
            return null;
        }
    }

    private Integer getHttpCode(final String errorRow) {
        try {
            return OBJECT_MAPPER.readTree(errorRow).get("httpCode").asInt();
        } catch (final NumberFormatException | JsonProcessingException ex) {
            return null;
        }
    }

    private String getConfigValue(String configKey) {
        return env.getProperty(configKey);
    }

}
