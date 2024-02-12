package com.nagarro.customerservice.dto;

import com.nagarro.customerservice.entity.CustomerEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerMapperV2 implements Function<CustomerDtoV2, CustomerEntity>  {
    /**
     * Applies this function to the given argument.
     *
     * @param customerDtoV2 the function argument
     * @return the function result
     */
    @Override
    public CustomerEntity apply(CustomerDtoV2 customerDtoV2) {
        return new CustomerEntity(customerDtoV2.name(), customerDtoV2.email(), customerDtoV2.address(), customerDtoV2.phoneNumber());
    }
}
