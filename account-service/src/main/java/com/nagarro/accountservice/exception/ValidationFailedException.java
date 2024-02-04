package com.nagarro.accountservice.exception;

public class ValidationFailedException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ValidationFailedException(String message){ super(message); }
}
