package com.nagarro.accountservice.feign;

import com.nagarro.accountservice.dto.CustomerDto;
import com.nagarro.accountservice.entity.CustomerEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("CUSTOMER-SERVICE")
public interface CustomerFeignClient {

    @GetMapping("api/v1/customer/{id}")
    public CustomerDto getCustomerById(@PathVariable Long id);

    @GetMapping("api/v1/customer/verify/{customerId}")
    public boolean verifyCustomer(@PathVariable Long customerId, @RequestParam String name, @RequestParam Long phoneNumber);

}
