package com.nagarro.accountservice.service;

import com.nagarro.accountservice.dao.AccountRepository;
import com.nagarro.accountservice.entity.AccountEntity;
import com.nagarro.accountservice.exception.ResourceNotFoundException;
import com.nagarro.accountservice.feign.CustomerFeignClient;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    AccountRepository accountRepository;
    CustomerFeignClient customerFeignClient;

    public AccountEntity createAccount(AccountEntity accountEntity) {
        return accountRepository.save(accountEntity);
    }

    public List<AccountEntity> getAllAccounts() {
        return accountRepository.findAll();
    }

    public AccountEntity getAccountById(Long id) {
        doesAccountExists(id);
        AccountEntity account = accountRepository.findById(id).get();
        //[TODO] //set up versioning of customer controller to get customer by id without calling back to Accoutn service. LOOP!
        return account;
    }

    public List<AccountEntity> getAllAccountsByUserId(Long userId) {
        return accountRepository.findAllByCustomerId(userId);
    }

    public ResponseEntity<?> deleteAccountById(Long id) {
        doesAccountExists(id);
        accountRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteAllAccountsByUserId(Long userId) {
        accountRepository.deleteAllByCustomerId(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void doesAccountExists(Long id) {
        if(!accountRepository.existsById(id)){
            throw new ResourceNotFoundException("Account Not Found");
        }
    }
}
