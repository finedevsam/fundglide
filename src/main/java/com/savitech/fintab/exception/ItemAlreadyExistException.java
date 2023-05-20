package com.savitech.fintab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ItemAlreadyExistException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ItemAlreadyExistException(String message){
        super(message);
    }
}
