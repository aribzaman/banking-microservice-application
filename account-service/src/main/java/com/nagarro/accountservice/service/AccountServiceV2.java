package com.nagarro.accountservice.service;

import com.nagarro.accountservice.dao.AccountRepository;
import com.nagarro.accountservice.dto.AccountDto;
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

    public List<AccountDto> getAllAccounts() {
        List<AccountEntity> allAccounts = accountRepository.findAll();
        return allAccounts.stream().map(accountMapper).toList();
    }

    public List<AccountEntity> getAllAccountsByUserId(Long userId) {
        return accountRepository.findAllByCustomerId(userId);
    }

    public AccountDto getAccountByCustomerId(Long customerId) {
        doesCustomerExists(customerId);
        return accountMapper.apply(accountRepository.findByCustomerId(customerId).get());
    }

    public AccountDto getAccountById(Long id) {
        doesAccountExists(id);
        AccountEntity account = accountRepository.findById(id).get();
        CustomerDto customer = customerFeignClient.getCustomerById(account.getCustomerId());
        AccountDto accountDto = accountMapper.apply(account);
        accountDto.setCustomer(customer);
        return accountDto;
    }

    public AccountDto createAccount(AccountEntity accountEntity) {
        accountEntity.setAccountNumber(Utility.generateAccountNumber());
        AccountEntity savedAccount = accountRepository.save(accountEntity);
        return accountMapper.apply(savedAccount);
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

    public void doesCustomerExists(Long id) {
        if(!accountRepository.existsByCustomerId(id)){
            throw new ResourceNotFoundException("Customer Account Not Found");
        }
    }

    private void doesAccountExists(Long id) {
        if(!accountRepository.existsById(id)){
            throw new ResourceNotFoundException("Account Not Found");
        }
    }

}
