package com.nagarro.accountservice.dto;

import com.nagarro.accountservice.entity.AccountEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AccountMapper implements Function<AccountEntity, AccountDto>{

    @Override
    public AccountDto apply(AccountEntity accountEntity) {
        return AccountDto.builder()
                .accountNumber(accountEntity.getAccountNumber())
                .ifscCode(accountEntity.getIfscCode())
                .branch(accountEntity.getBranch())
                .city(accountEntity.getCity())
                .customerId(accountEntity.getCustomerId())
                .balance(accountEntity.getBalance())
                .createdAt(accountEntity.getCreatedAt())
                .build();
    }
}
