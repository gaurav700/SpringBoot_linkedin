package com.LinkedIn.UserService.Exceptions;

public class BadRequestException extends  RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
