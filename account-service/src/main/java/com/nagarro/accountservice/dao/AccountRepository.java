package com.nagarro.accountservice.dao;

import com.nagarro.accountservice.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByAccountNumber(Long accountNumber);

    void deleteByAccountNumber(Long accountNumber);

    void deleteAllByCustomerId(Long customerId);

    boolean existsByAccountNumber(Long accountNumber);

    //----------------------------------------------------------------

    boolean existsByCustomerId(Long customerId);

    List<AccountEntity> findAllByCustomerId(Long customerId);

    void deleteByCustomerId(Long customerId);

}
