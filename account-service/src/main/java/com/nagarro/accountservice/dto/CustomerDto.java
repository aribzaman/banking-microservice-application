package com.nagarro.accountservice.dto;

public record CustomerDto(Long id, String name, String email, String address, Long phoneNumber) {
}