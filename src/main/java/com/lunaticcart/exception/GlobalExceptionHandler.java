package com.lunaticcart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException exception)
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(exception.getMessage());
	}
	
	@ExceptionHandler(
	        org.springframework.web.bind.MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationErrors(
	        org.springframework.web.bind.MethodArgumentNotValidException exception) {

	    String message = exception.getBindingResult()
	            .getFieldErrors()
	            .get(0)
	            .getDefaultMessage();

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(message);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(
	        IllegalArgumentException exception) {

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(exception.getMessage());
	}

}
