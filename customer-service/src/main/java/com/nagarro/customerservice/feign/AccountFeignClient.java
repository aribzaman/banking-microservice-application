package com.nagarro.customerservice.feign;

import com.nagarro.customerservice.entity.AccountEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("ACCOUNT-SERVICE")
public interface AccountFeignClient {

    @DeleteMapping("/api/v1/account/customer/{customerId}")
    ResponseEntity<?> deleteAllAccountsByCustomerId(@PathVariable Long customerId);

    //----------------------------------------------------------------

    @GetMapping("/api/v2/account")
    List<AccountEntity> getAllAccounts();

    @GetMapping("/api/v2/account/customer/{customerId}")
    public List<AccountEntity> getAllAccountsByCustomerId(@PathVariable Long customerId);

}
