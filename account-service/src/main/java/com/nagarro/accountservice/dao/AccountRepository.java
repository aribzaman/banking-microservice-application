package com.nagarro.accountservice.dao;

import com.nagarro.accountservice.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    List<AccountEntity> findAllByCustomerId(Long customerId);

    void deleteAllByCustomerId(Long customerId);
}
