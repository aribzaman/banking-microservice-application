package com.nagarro.customerservice.service.implementation;

import com.nagarro.customerservice.dao.CustomerRepository;
import com.nagarro.customerservice.dto.CustomerDto;
import com.nagarro.customerservice.dto.CustomerMapper;
import com.nagarro.customerservice.entity.AccountEntity;
import com.nagarro.customerservice.entity.CustomerEntity;
import com.nagarro.customerservice.exception.ResourceNotFoundException;
import com.nagarro.customerservice.feign.AccountFeignClient;
import com.nagarro.customerservice.service.CustomerServiceV2;
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
public class CustomerServiceV2Impl implements CustomerServiceV2 {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    AccountFeignClient accountFeignClient;

    @Override
    public List<CustomerEntity> getAllCustomer() {
        List<CustomerEntity> allCustomers = customerRepository.findAll();
        List<AccountEntity> allAccounts = accountFeignClient.getAllAccounts();
        Utility.populateCustomerAccounts(allCustomers, allAccounts);
        return allCustomers;
    }

    @Override
    public CustomerEntity getCustomerById(Long customerId) {
        doesCustomerExists(customerId);
        CustomerEntity customer = customerRepository.findById(customerId).get();
        List<AccountEntity> accounts = accountFeignClient.getAllAccountsByCustomerId(customerId);
        customer.setAccounts(accounts);
        return customer;
    }

    @Override
    public CustomerEntity createCustomer(CustomerDto customerDto) {
        CustomerEntity customerEntity = customerMapper.apply(customerDto);
        return customerRepository.save(customerEntity);
    }

    @Override
    public CustomerEntity updateCustomer(Long id, CustomerDto customerDto) {

        CustomerEntity existingCustomer = getCustomerById(id);
        CustomerEntity updatedCustomer = customerMapper.apply(customerDto);

        BeanUtils.copyProperties(updatedCustomer, existingCustomer, getNullPropertyNames(updatedCustomer));
        CustomerEntity  cs = customerRepository.save(existingCustomer);
        cs.setAccounts(null);
        return cs;
    }

    @Override
    public ResponseEntity<?> deleteCustomer(Long id) {
        doesCustomerExists(id);
        accountFeignClient.deleteAllAccountsByCustomerId(id);
        customerRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void doesCustomerExists(Long id) {
        if(!customerRepository.existsById(id)){
            throw new ResourceNotFoundException("Customer Not Found");
        }
    }
}
