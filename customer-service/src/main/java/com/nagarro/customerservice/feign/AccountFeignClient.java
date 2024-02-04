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

    @DeleteMapping("/account/user/{customerId}")
    ResponseEntity<?> deleteAllAccountsByUserId(@PathVariable Long customerId);

    //----------------------------------------------------------------

    @GetMapping("/account")
    List<AccountEntity> getAllAccounts();

    @GetMapping("/account/user/{userId}")
    List<AccountEntity> getAllAccountsByUserId(@PathVariable Long userId);

}
