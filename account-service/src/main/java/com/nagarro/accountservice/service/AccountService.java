package com.nagarro.accountservice.service;

import com.nagarro.accountservice.dto.AccountDto;
import com.nagarro.accountservice.dto.ResponseMessage;
import com.nagarro.accountservice.dto.TransactionDto;
import com.nagarro.accountservice.entity.AccountEntity;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    AccountDto getAccountByAccountNumber(Long accountNumber);

    ResponseEntity<ResponseMessage> transaction(TransactionDto transactionDto);

    @Transactional
    ResponseEntity<?> deleteAccountByAccountNumber(Long accountNumber);

    @Transactional
    ResponseEntity<?> deleteAllAccountsByCustomerId(Long customerId);

    AccountDto createAccount(AccountEntity accountEntity);
}
