package com.nagarro.customerservice.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ResponseMessage(HttpStatus status, int statusCode, LocalDateTime timestamp, String message, String description) {}