package com.nagarro.customerservice.dto;

import com.nagarro.customerservice.entity.CustomerEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerMapper implements Function<CustomerDto, CustomerEntity> {

    @Override
    public CustomerEntity apply(CustomerDto customerDto) {
        return new CustomerEntity(customerDto.name(), customerDto.email(), customerDto.address(), customerDto.phoneNumber());
    }

    public CustomerDto reverse(CustomerEntity customerEntity) {
        return new CustomerDto(customerEntity.getId(), customerEntity.getName(), customerEntity.getEmail(), customerEntity.getAddress(), customerEntity.getPhoneNumber());
    }
}
