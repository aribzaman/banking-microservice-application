package com.nagarro.customerservice.feign;

import com.nagarro.customerservice.entity.AccountEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(contextId = "AccountFeignClientV2", value = "ACCOUNT-SERVICE", path = "/api/v2/account")
public interface AccountFeignClientV2 {

    @GetMapping
    List<AccountEntity> getAllAccounts();

    @GetMapping("customer/{customerId}")
    List<AccountEntity> getAllAccountsByCustomerId(@PathVariable Long customerId);

    @PostMapping
    AccountEntity createCustomer(@RequestBody AccountEntity accountEntity);


}
