package com.nagarro.customerservice.dao;

import com.nagarro.customerservice.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    boolean existsByIdAndNameAndPhoneNumber(Long customerId, String name, Long phoneNumber);
}
