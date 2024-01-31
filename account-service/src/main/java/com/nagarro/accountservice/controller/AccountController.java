package com.nagarro.accountservice.controller;

import com.nagarro.accountservice.entity.AccountEntity;
import com.nagarro.accountservice.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    AccountService accountService;

    @GetMapping
    public List<AccountEntity> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public AccountEntity getAccountById(@PathVariable Long id){
        return accountService.getAccountById(id);
    }

    @GetMapping("/user/{userId}")
    public List<AccountEntity> getAllAccountsByUserId(@PathVariable Long userId){
        return accountService.getAllAccountsByUserId(userId);
    }

    @PostMapping
    public AccountEntity createCustomer(@RequestBody AccountEntity accountEntity){
        return accountService.createAccount(accountEntity);
    }

    @PutMapping("/credit/{accountNumber}")
    public ResponseEntity<?> credit(@PathVariable Long accountNumber){
        //[TODO]
        return null;
    }

    @PutMapping("/debit/{accountNumber}")
    public ResponseEntity<?> debit(@PathVariable Long accountNumber){
        //[TODO]
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccountById(@PathVariable Long id){
        return accountService.deleteAccountById(id);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteAllAccountsByUserId(@PathVariable Long userId){
        return accountService.deleteAllAccountsByUserId(userId);
    }


}
