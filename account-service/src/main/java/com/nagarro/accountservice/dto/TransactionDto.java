package com.nagarro.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    //Account Details
    Long accountNumber;
    String ifscCode;
    String branch;

    //Customer Details
    String name;
    Long phoneNumber;

    //Transaction Details
    Double amount;
    String transactionType;

}
