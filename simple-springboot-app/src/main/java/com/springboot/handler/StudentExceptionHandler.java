package com.springboot.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.springboot.exception.InvalidFieldException;
import com.springboot.exception.InvalidHeaderFieldException;

@RestControllerAdvice
public class StudentExceptionHandler {

	@ExceptionHandler
	public String handleInvalidFieldException(InvalidFieldException exception) {
		System.out.println("666666666666666666666666");
		return exception.getMessage();
	}

	@ExceptionHandler
	public ResponseEntity<String> handleInvalidHeaderFieldException(InvalidHeaderFieldException exception) {
		System.out.println("3333333333333333333333");
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.PRECONDITION_FAILED);
	}
	@ExceptionHandler
	public ResponseEntity<String> DefaultException(Exception e){
		return new ResponseEntity<>(e.toString(), HttpStatus.PRECONDITION_FAILED);
	}
}