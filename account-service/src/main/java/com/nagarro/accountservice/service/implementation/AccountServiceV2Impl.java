package com.nagarro.accountservice.service.implementation;

import com.nagarro.accountservice.dao.AccountRepository;
import com.nagarro.accountservice.dto.AccountMapper;
import com.nagarro.accountservice.dto.CustomerDto;
import com.nagarro.accountservice.entity.AccountEntity;
import com.nagarro.accountservice.exception.ResourceNotFoundException;
import com.nagarro.accountservice.feign.CustomerFeignClient;
import com.nagarro.accountservice.service.AccountServiceV2;
import com.nagarro.accountservice.utility.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceV2Impl implements AccountServiceV2 {

    AccountRepository accountRepository;
    CustomerFeignClient customerFeignClient;
    AccountMapper accountMapper;

    @Override
    public List<AccountEntity> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public AccountEntity getAccountById(Long id) {
        doesAccountExists(id);
        AccountEntity account = accountRepository.findById(id).get();
        CustomerDto customer = customerFeignClient.getCustomerById(account.getCustomerId());
        account.setCustomer(customer);
        return account;
    }

    @Override
    public List<AccountEntity> getAllAccountsByCustomerId(Long customerId) {
        doesCustomerExists(customerId);
        return accountRepository.findAllByCustomerId(customerId);
    }

    @Override
    public AccountEntity createAccount(AccountEntity accountEntity) {
        accountEntity.setAccountNumber(Utility.generateAccountNumber());
        customerFeignClient.verifyCustomer(accountEntity.getCustomerId(), "",1L);
        AccountEntity savedAccount = accountRepository.save(accountEntity);
        return savedAccount;
    }

    @Override
    public ResponseEntity<?> deleteAccountById(Long id) {
        doesAccountExists(id);
        accountRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void doesCustomerExists(Long customerId) {
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
