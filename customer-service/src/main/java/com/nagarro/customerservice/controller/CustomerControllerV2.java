package com.nagarro.customerservice.controller;

import com.nagarro.customerservice.dto.CustomerDto;
import com.nagarro.customerservice.entity.CustomerEntity;
import com.nagarro.customerservice.exception.ResourceNotFoundException;
import com.nagarro.customerservice.service.CustomerService;
import com.nagarro.customerservice.service.CustomerServiceV2;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/customer")
@AllArgsConstructor
public class CustomerControllerV2 {

    CustomerServiceV2 customerService;

    @GetMapping
    public List<CustomerDto> getAllCustomer(){
        return customerService.getAllCustomer();
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomerById(@PathVariable Long id){
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto){
        return customerService.createCustomer(customerDto);
    }

    @PutMapping("/{id}")
    public CustomerDto updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto){
        return customerService.updateCustomer(id, customerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomer(id);
    }

    @GetMapping("/verify/{customerId}")
    public boolean verifyCustomer(@PathVariable Long customerId, @RequestParam String name, @RequestParam Long phoneNumber){
        return customerService.verifyCustomer(customerId, name, phoneNumber);
    }

}
