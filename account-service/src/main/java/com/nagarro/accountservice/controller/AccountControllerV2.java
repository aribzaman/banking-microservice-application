package com.nagarro.accountservice.controller;

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

    @GetMapping
    public List<AccountEntity> getAllAccounts(){
        return accountServiceV2.getAllAccounts();
    }

    @GetMapping("/{id}")
    public AccountEntity getAccountById(@PathVariable Long id){
        return accountServiceV2.getAccountById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<AccountEntity> getAllAccountsByCustomerId(@PathVariable Long customerId){
        return accountServiceV2.getAllAccountsByCustomerId(customerId);
    }

    @PostMapping
    public AccountEntity createCustomer(@RequestBody AccountEntity accountEntity){
        return accountServiceV2.createAccount(accountEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccountById(@PathVariable Long id){
        return accountServiceV2.deleteAccountById(id);
    }

}
