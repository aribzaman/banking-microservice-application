package com.nagarro.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseMessage(HttpStatus status, int statusCode, LocalDateTime timestamp, String message, String description) {}