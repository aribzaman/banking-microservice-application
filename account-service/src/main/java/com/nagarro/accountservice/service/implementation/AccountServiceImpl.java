package com.nagarro.accountservice.service.implementation;

import com.nagarro.accountservice.dao.AccountRepository;
import com.nagarro.accountservice.dto.*;
import com.nagarro.accountservice.entity.AccountEntity;
import com.nagarro.accountservice.exception.ResourceNotFoundException;
import com.nagarro.accountservice.exception.ValidationFailedException;
import com.nagarro.accountservice.feign.CustomerFeignClient;
import com.nagarro.accountservice.service.AccountService;
import com.nagarro.accountservice.utility.Utility;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    CustomerFeignClient customerFeignClient;
    AccountMapper accountMapper;

    @Override
    public AccountDto getAccountByAccountNumber(Long accountNumber) {
        doesAccountNumberExists(accountNumber);
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber).get();
        CustomerDto customerDto = customerFeignClient.getCustomerById(account.getCustomerId());
        AccountDto accountDto = accountMapper.apply(account);
        accountDto.setCustomer(customerDto);
        return accountDto;
    }

    @Override
    public ResponseEntity<ResponseMessage> transaction(TransactionDto transactionDto) {
        doesAccountNumberExists(transactionDto.getAccountNumber());
        AccountEntity account = accountRepository.findByAccountNumber(transactionDto.getAccountNumber()).get();
        verifyAccount(account, transactionDto);
        verifyCustomer(account, transactionDto);
        updateBalance(transactionDto, account);
        accountRepository.save(account);

        ResponseMessage message = new ResponseMessage(HttpStatus.OK, HttpStatus.OK.value(),
                LocalDateTime.now(), "Transaction completed successfully!",null);

        return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
    }

    private static void updateBalance(TransactionDto transactionDto, AccountEntity account) {
        double amount;
        if(transactionDto.getAmount()<1){ throw new ValidationFailedException("Amount Should Be Greater than 0");}
        if(transactionDto.getTransactionType().equalsIgnoreCase("debit")){
            if(transactionDto.getAmount() > account.getBalance()){
                throw new ValidationFailedException("Insufficient Balance in account.");
            }
            amount = -transactionDto.getAmount();
        }
        else if(transactionDto.getTransactionType().equalsIgnoreCase("credit")){
            amount = transactionDto.getAmount();
        }
        else{
            throw new ValidationFailedException("Wrong transaction type : Choose DEBIT / CREDIT");
        }
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
    }

    private static void verifyAccount(AccountEntity account, TransactionDto transactionDto) {
        if(!account.getBranch().equalsIgnoreCase(transactionDto.getBranch()) ||
                !account.getIfscCode().equalsIgnoreCase(transactionDto.getIfscCode()))
        {
            throw new ValidationFailedException("Invalid Account Details");}
    }

    private void verifyCustomer(AccountEntity account, TransactionDto transactionDto) {
        if(!customerFeignClient.verifyCustomer(account.getCustomerId(), transactionDto.getName(), transactionDto.getPhoneNumber())){
            throw new ValidationFailedException("Invalid Customer Details");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAccountByAccountNumber(Long accountNumber) {
        doesAccountNumberExists(accountNumber);
        accountRepository.deleteByAccountNumber(accountNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAllAccountsByCustomerId(Long customerId) {
        //[FEATURE] check if any account exists by this customer id (Mandatory: add account when adding customer)
        //accountServiceV2.doesCustomerExists(customerId);
        accountRepository.deleteAllByCustomerId(customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void doesAccountNumberExists(Long id) {
        if(!accountRepository.existsByAccountNumber(id)){
            throw new ResourceNotFoundException("No Customer Account Found by A/C number: "+ id);
        }
    }

    //----------------------------------------------------------------

    @Override
    public AccountDto createAccount(AccountEntity accountEntity) {
        accountEntity.setAccountNumber(Utility.generateAccountNumber());
        customerFeignClient.verifyCustomer(accountEntity.getCustomerId(), "",1L);
        AccountEntity savedAccount = accountRepository.save(accountEntity);
        return accountMapper.apply(savedAccount);
    }

}
