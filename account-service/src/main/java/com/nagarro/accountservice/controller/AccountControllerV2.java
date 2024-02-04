package com.nagarro.accountservice.controller;

import com.nagarro.accountservice.dto.AccountDto;
import com.nagarro.accountservice.entity.AccountEntity;
import com.nagarro.accountservice.service.AccountServiceV2;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/account")
@AllArgsConstructor
public class AccountControllerV2 {

    AccountServiceV2 accountServiceV2;

    @GetMapping("/{id}")
    public AccountDto getAccountById(@PathVariable Long id){
        return accountServiceV2.getAccountById(id);
    }

    @GetMapping
    public List<AccountDto> getAllAccounts(){
        return accountServiceV2.getAllAccounts();
    }

    @GetMapping("/customer/{customerId}")
    public AccountDto getAccountByCustomerId(@PathVariable Long customerId){
        return accountServiceV2.getAccountByCustomerId(customerId);
    }

    @GetMapping("/user/{userId}")
    public List<AccountEntity> getAllAccountsByUserId(@PathVariable Long userId){
        return accountServiceV2.getAllAccountsByUserId(userId);
    }

    @PostMapping
    public AccountDto createCustomer(@RequestBody AccountEntity accountEntity){
        return accountServiceV2.createAccount(accountEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccountById(@PathVariable Long id){
        return accountServiceV2.deleteAccountById(id);
    }

}
