package com.nagarro.customerservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountEntity {

    private Long id;
    private Long accountNumber;
    private Long ifscCode;
    private Long customerId;
    private double balance;

}
