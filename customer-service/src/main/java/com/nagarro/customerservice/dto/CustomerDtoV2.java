package com.nagarro.customerservice.dto;

public record CustomerDtoV2(Long id, String name, String email, String address, Long phoneNumber, String ifscCode, String branch, String city, Double amount) {
}
