package com.nagarro.accountservice.controller;

import com.nagarro.accountservice.dto.AccountDto;
import com.nagarro.accountservice.dto.ResponseMessage;
import com.nagarro.accountservice.dto.TransactionDto;
import com.nagarro.accountservice.entity.AccountEntity;
import com.nagarro.accountservice.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@AllArgsConstructor
public class AccountController {

    AccountService accountService;

    @GetMapping("/{accountNumber}")
    public AccountDto getAccountByAccountNumber(@PathVariable Long accountNumber){
        return accountService.getAccountByAccountNumber(accountNumber);
    }

    @PutMapping("/transaction")
    public ResponseEntity<ResponseMessage> transaction(@RequestBody TransactionDto transactionDto){
        return accountService.transaction(transactionDto);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<?> deleteAccountsByAccountNumber(@PathVariable Long accountNumber){
        return accountService.deleteAccountByAccountNumber(accountNumber);
    }

    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<?> deleteAllAccountsByCustomerId(@PathVariable Long customerId){
        return accountService.deleteAllAccountsByCustomerId(customerId);
    }

    //----------------------------------------------------------------

    @PostMapping
    public AccountDto createCustomer(@RequestBody AccountEntity accountEntity){
        return accountService.createAccount(accountEntity);
    }

}
