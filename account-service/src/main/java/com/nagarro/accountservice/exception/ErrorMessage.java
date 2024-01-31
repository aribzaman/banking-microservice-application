package com.nagarro.accountservice.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorMessage(HttpStatus status, int statusCode, LocalDateTime timestamp, String message, String description) {}