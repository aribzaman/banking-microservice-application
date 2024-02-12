package com.nagarro.customerservice.controller;

import com.nagarro.customerservice.dto.CustomerDto;
import com.nagarro.customerservice.dto.CustomerDtoV2;
import com.nagarro.customerservice.entity.CustomerEntity;
import com.nagarro.customerservice.service.CustomerServiceV2;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/customer")
@AllArgsConstructor
@Validated
public class CustomerControllerV2 {

    CustomerServiceV2 customerServiceV2;

    @GetMapping
    public List<CustomerEntity> getAllCustomer(){
        return customerServiceV2.getAllCustomer();
    }

    @GetMapping("/{customerId}")
    public CustomerEntity getCustomerById(@PathVariable Long customerId){
        return customerServiceV2.getCustomerById(customerId);
    }

    @PostMapping
    public CustomerEntity createCustomer(@RequestBody CustomerDtoV2 customerDto){
        return customerServiceV2.createCustomer(customerDto);
    }

    @PutMapping("/{id}")
    public CustomerEntity updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto){
        return customerServiceV2.updateCustomer(id, customerDto);
    }

}
