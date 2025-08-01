package com.boti.productmanagerapp.adapters.in.controller;

import com.boti.productmanagerapp.adapters.in.web.response.ErrorResponse;
import com.boti.productmanagerapp.application.core.exceptions.ProductAlreadyExistsException;
import com.boti.productmanagerapp.application.core.exceptions.ProductNotFoundException;
import com.boti.productmanagerapp.application.ports.out.LoggerPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerAdviceExceptionHandler {

    @Autowired
    private LoggerPort loggerPort;

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyExistsException(
            ProductAlreadyExistsException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        String errorMessage = ex.getMessage();
        String requestPath = request.getRequestURI();

        loggerPort.warn(ControllerAdviceExceptionHandler.class, String.format("CONFLICT: %s - Path: %s", errorMessage, requestPath));

        ErrorResponse errorResponse = new ErrorResponse(status, errorMessage, requestPath);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        String errorMessage = ex.getMessage();
        String requestPath = request.getRequestURI();

        loggerPort.warn(ControllerAdviceExceptionHandler.class,String.format("Product not found: %s - Path: %s", errorMessage, requestPath));

        ErrorResponse errorResponse = new ErrorResponse(status, errorMessage, requestPath);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(
            Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST; // 400
        String errorMessage = ex.getMessage();
        String requestPath = request.getRequestURI();

        loggerPort.error(ControllerAdviceExceptionHandler.class, String.format("Bad Request: &s - path: %s", errorMessage, requestPath, ex), ex);

        ErrorResponse errorResponse = new ErrorResponse(status, errorMessage, requestPath);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        String errorMessage = "An unexpected error occurred: " + ex.getMessage();
        String requestPath = request.getRequestURI();

        loggerPort.error(ControllerAdviceExceptionHandler.class, String.format("INTERNAL SERVER ERROR: %s - Path: %s", errorMessage, requestPath, ex), ex);

        ErrorResponse errorResponse = new ErrorResponse(status, errorMessage, requestPath);
        return new ResponseEntity<>(errorResponse, status);
    }
}