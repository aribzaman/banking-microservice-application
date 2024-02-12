package com.nagarro.customerservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "AccountFeignClient", value = "ACCOUNT-SERVICE", path = "/api/v1/account")
public interface AccountFeignClient {

    @DeleteMapping("/customer/{customerId}")
    ResponseEntity<?> deleteAllAccountsByCustomerId(@PathVariable Long customerId);

}
