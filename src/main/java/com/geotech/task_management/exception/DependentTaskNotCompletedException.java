package com.geotech.task_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DependentTaskNotCompletedException extends RuntimeException{

    public DependentTaskNotCompletedException(String message){
        super(message);
    }

}
