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

    @GetMapping("/account")
    public List<AccountEntity> getAllAccounts();

    @GetMapping("/account/user/{userId}")
    public List<AccountEntity> getAllAccountsByUserId(@PathVariable Long userId);

    @DeleteMapping("/account/user/{userId}")
    public ResponseEntity<?> deleteAllAccountsByUserId(@PathVariable Long userId);

}
