package com.nagarro.accountservice.service;

import com.nagarro.accountservice.dao.AccountRepository;
import com.nagarro.accountservice.dto.AccountMapper;
import com.nagarro.accountservice.dto.CustomerDto;
import com.nagarro.accountservice.entity.AccountEntity;
import com.nagarro.accountservice.exception.ResourceNotFoundException;
import com.nagarro.accountservice.feign.CustomerFeignClient;
import com.nagarro.accountservice.utility.Utility;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceV2 {

    AccountRepository accountRepository;
    CustomerFeignClient customerFeignClient;
    AccountMapper accountMapper;

    public List<AccountEntity> getAllAccounts() {
//        List<AccountEntity> allAccounts = accountRepository.findAll();
//        return allAccounts.stream().map(accountMapper).toList();
        return accountRepository.findAll();
    }

    public AccountEntity getAccountById(Long id) {
        doesAccountExists(id);
        AccountEntity account = accountRepository.findById(id).get();
        CustomerDto customer = customerFeignClient.getCustomerById(account.getCustomerId());
//        AccountDto accountDto = accountMapper.apply(account);
//        accountDto.setCustomer(customer);
//        return accountDto;
        account.setCustomer(customer);
        return account;
    }

    public List<AccountEntity> getAllAccountsByCustomerId(Long customerId) {
        return accountRepository.findAllByCustomerId(customerId);
    }

    public AccountEntity createAccount(AccountEntity accountEntity) {
        accountEntity.setAccountNumber(Utility.generateAccountNumber());
        customerFeignClient.verifyCustomer(accountEntity.getCustomerId(), "",1L);
        AccountEntity savedAccount = accountRepository.save(accountEntity);
        return savedAccount;
//        return accountMapper.apply(savedAccount);
    }

    public ResponseEntity<?> deleteAccountById(Long id) {
        doesAccountExists(id);
        accountRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteAccountByCustomerId(Long customerId) {
        doesCustomerExists(customerId);
        accountRepository.deleteByCustomerId(customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void doesCustomerExists(Long customerId) {
        if(!accountRepository.existsByCustomerId(customerId)){
            throw new ResourceNotFoundException("No Customer Account Found with customerId: " + customerId);
        }
    }

    private void doesAccountExists(Long id) {
        if(!accountRepository.existsById(id)){
            throw new ResourceNotFoundException("Account Not Found");
        }
    }

}
