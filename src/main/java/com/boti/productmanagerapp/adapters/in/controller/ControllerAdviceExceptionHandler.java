package com.boti.productmanagerapp.adapters.in.controller;

import com.boti.productmanagerapp.adapters.in.web.response.ErrorResponse;
import com.boti.productmanagerapp.application.core.exceptions.ProductAlreadyExistsException;
import com.boti.productmanagerapp.application.core.exceptions.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerAdviceExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ControllerAdviceExceptionHandler.class);

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyExistsException(
            ProductAlreadyExistsException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        String errorMessage = ex.getMessage();
        String requestPath = request.getRequestURI();

        log.warn("CONFLICT: {} - Path: {}", errorMessage, requestPath);

        ErrorResponse errorResponse = new ErrorResponse(status, errorMessage, requestPath);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        String errorMessage = ex.getMessage();
        String requestPath = request.getRequestURI();

        log.warn("Product not found: {} - Path: {}", errorMessage, requestPath);

        ErrorResponse errorResponse = new ErrorResponse(status, errorMessage, requestPath);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        String errorMessage = "An unexpected error occurred: " + ex.getMessage();
        String requestPath = request.getRequestURI();

        log.error("INTERNAL SERVER ERROR: {} - Path: {}", errorMessage, requestPath, ex);

        ErrorResponse errorResponse = new ErrorResponse(status, errorMessage, requestPath);
        return new ResponseEntity<>(errorResponse, status);
    }
}