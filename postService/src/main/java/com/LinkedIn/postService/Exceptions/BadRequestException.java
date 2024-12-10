package com.LinkedIn.postService.Exceptions;

public class BadRequestException extends  RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
