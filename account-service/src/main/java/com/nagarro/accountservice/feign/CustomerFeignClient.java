package com.nagarro.accountservice.feign;

import com.nagarro.accountservice.entity.CustomerEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("CUSTOMER-SERVICE")
public interface CustomerFeignClient {

    @GetMapping("customer/{id}")
    public CustomerEntity getCustomerById(@PathVariable Long id);
}
