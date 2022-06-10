package com.alterra.capstoneproject.util;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.alterra.capstoneproject.domain.common.ApiResponse;

public class ResponseUtil {
    private ResponseUtil() {}

    public static <T> ResponseEntity<Object> build(String message, T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(build(message, data), httpStatus);
    }

    private static <T> ApiResponse<T> build(String message, T data) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .data(data)
                .build();
    }
}