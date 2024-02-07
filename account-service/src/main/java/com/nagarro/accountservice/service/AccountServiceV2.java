package com.nagarro.accountservice.service;

import com.nagarro.accountservice.entity.AccountEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountServiceV2 {
    List<AccountEntity> getAllAccounts();

    AccountEntity getAccountById(Long id);

    List<AccountEntity> getAllAccountsByCustomerId(Long customerId);

    AccountEntity createAccount(AccountEntity accountEntity);

    ResponseEntity<?> deleteAccountById(Long id);

}
