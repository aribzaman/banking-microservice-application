package com.nagarro.customerservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountEntity {

    private Long id;
    private Long accountNumber;
    private String ifscCode;
    private String branch;
    private String city;
    private Long customerId;
    private double balance;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;

    public AccountEntity(String ifscCode, String branch, String city, Long customerId, Double balance) {
        this.ifscCode = ifscCode;
        this.branch = branch;
        this.city = city;
        this.customerId = customerId;
        this.balance = balance;
    }
}
