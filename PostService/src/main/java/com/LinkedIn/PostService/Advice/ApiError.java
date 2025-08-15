package com.LinkedIn.PostService.Advice;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {
    private LocalDateTime timeStamp;
    private String error;
    private HttpStatus httpStatus;

    public ApiError(){
        this.timeStamp = LocalDateTime.now();
    }

    public ApiError(String error , HttpStatus httpStatus){
        this();
        this.error = error;
        this.httpStatus = httpStatus;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "timeStamp=" + timeStamp +
                ", error='" + error + '\'' +
                ", httpStatus=" + httpStatus +
                '}';
    }
}
