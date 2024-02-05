package com.nagarro.customerservice.dto;

public record CustomerDto(Long id, String name, String email, String address, Long phoneNumber) {
}