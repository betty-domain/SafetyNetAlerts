package com.safetynet.alerts.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Function;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FunctionalException extends RuntimeException{
    public FunctionalException(String message)
    {
        super(message);
    }
}
