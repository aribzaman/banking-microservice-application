package com.nagarro.customerservice.service;

import com.nagarro.customerservice.dao.CustomerRepository;
import com.nagarro.customerservice.dto.CustomerDto;
import com.nagarro.customerservice.dto.CustomerMapper;
import com.nagarro.customerservice.entity.AccountEntity;
import com.nagarro.customerservice.entity.CustomerEntity;
import com.nagarro.customerservice.exception.ResourceNotFoundException;
import com.nagarro.customerservice.feign.AccountFeignClient;
import com.nagarro.customerservice.utility.Utility;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nagarro.customerservice.utility.Utility.getNullPropertyNames;

@Service
@AllArgsConstructor
public class CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    AccountFeignClient accountFeignClient;

    public List<CustomerEntity> getAllCustomer() {
        List<CustomerEntity> allCustomers = customerRepository.findAll();
        List<AccountEntity> allAccounts = accountFeignClient.getAllAccounts();
        Utility.populateCustomerAccounts(allCustomers, allAccounts);

        return allCustomers;
    }

    public CustomerEntity getCustomerById(Long id) {
        doesCustomerExists(id);
        CustomerEntity customer = customerRepository.findById(id).get();
        List<AccountEntity> accounts = accountFeignClient.getAllAccountsByUserId(id);
        customer.setAccounts(accounts);
        return customer;
    }

    public CustomerEntity createCustomer(CustomerDto customerDto) {
        CustomerEntity customerEntity = customerMapper.apply(customerDto);
        return customerRepository.save(customerEntity);
    }

    public CustomerEntity updateCustomer(Long id, CustomerDto customerDto) {

        CustomerEntity existingCustomer = getCustomerById(id);
        CustomerEntity updatedCustomer = customerMapper.apply(customerDto);

        BeanUtils.copyProperties(updatedCustomer, existingCustomer, getNullPropertyNames(updatedCustomer));

        return customerRepository.save(existingCustomer);
    }

    public ResponseEntity<?> deleteCustomer(Long id) {
        doesCustomerExists(id);
        accountFeignClient.deleteAllAccountsByUserId(id);
        customerRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void doesCustomerExists(Long id) {
        if(!customerRepository.existsById(id)){
            throw new ResourceNotFoundException("Customer Not Found");
        }
    }
}
