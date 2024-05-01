package com.example.tci.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ParseDateException extends RuntimeException{
	public ParseDateException(String message) {
		super(message);
	}

}
