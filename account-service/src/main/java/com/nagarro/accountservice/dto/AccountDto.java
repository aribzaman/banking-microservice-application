package com.nagarro.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Data
@Builder
public class AccountDto {

    private Long accountNumber;
    private String ifscCode;
    private String branch;
    private String city;
    private Long customerId;
    private double balance;
    private LocalDateTime createdAt;
    private CustomerDto customer;
}
