package com.nagarro.customerservice.service;

import com.nagarro.customerservice.dto.CustomerDto;
import com.nagarro.customerservice.entity.CustomerEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerServiceV2 {
    List<CustomerEntity> getAllCustomer();

    CustomerEntity getCustomerById(Long customerId);

    CustomerEntity createCustomer(CustomerDto customerDto);

    CustomerEntity updateCustomer(Long id, CustomerDto customerDto);

    ResponseEntity<?> deleteCustomer(Long id);

}
