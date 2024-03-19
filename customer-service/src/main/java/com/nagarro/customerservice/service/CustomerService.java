package com.nagarro.customerservice.service;

import com.nagarro.customerservice.dto.CustomerDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomer();

    CustomerDto getCustomerById(Long id);

    CustomerDto createCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(Long customerId, CustomerDto customerDto);

    boolean verifyCustomer(Long customerId, String name, Long phoneNumber);

    void deleteCustomer(Long id);

}
