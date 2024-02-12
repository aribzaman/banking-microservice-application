package com.nagarro.customerservice.service.implementation;

import com.nagarro.customerservice.dao.CustomerRepository;
import com.nagarro.customerservice.dto.CustomerDto;
import com.nagarro.customerservice.dto.CustomerDtoV2;
import com.nagarro.customerservice.dto.CustomerMapper;
import com.nagarro.customerservice.dto.CustomerMapperV2;
import com.nagarro.customerservice.entity.AccountEntity;
import com.nagarro.customerservice.entity.CustomerEntity;
import com.nagarro.customerservice.exception.ResourceNotFoundException;
import com.nagarro.customerservice.feign.AccountFeignClientV2;
import com.nagarro.customerservice.service.CustomerServiceV2;
import com.nagarro.customerservice.utility.Utility;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.nagarro.customerservice.utility.Utility.getNullPropertyNames;

@Service
@AllArgsConstructor
public class CustomerServiceV2Impl implements CustomerServiceV2 {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    CustomerMapperV2 customerMapperV2;
    AccountFeignClientV2 accountFeignClientV2;

    @Override
    public List<CustomerEntity> getAllCustomer() {
        List<CustomerEntity> allCustomers = customerRepository.findAll();
        List<AccountEntity> allAccounts = accountFeignClientV2.getAllAccounts();
        Utility.populateCustomerAccounts(allCustomers, allAccounts);
        return allCustomers;
    }

    @Override
    public CustomerEntity getCustomerById(Long customerId) {
        doesCustomerExists(customerId);
        CustomerEntity customer = customerRepository.findById(customerId).get();
        List<AccountEntity> accounts = accountFeignClientV2.getAllAccountsByCustomerId(customerId);
        customer.setAccounts(accounts);
        return customer;
    }

    @Override
    public CustomerEntity createCustomer(CustomerDtoV2 customerDto) {
        CustomerEntity customerEntity = customerMapperV2.apply(customerDto);
        CustomerEntity createdCustomer = customerRepository.save(customerEntity);
        AccountEntity accountEntity = new AccountEntity(customerDto.ifscCode(), customerDto.city(), customerDto.branch(), createdCustomer.getId(), customerDto.amount());
        AccountEntity ac = accountFeignClientV2.createCustomer(accountEntity);
        createdCustomer.setAccounts(List.of(ac));
        return createdCustomer;
    }

    @Override
    public CustomerEntity updateCustomer(Long id, CustomerDto customerDto) {

        CustomerEntity existingCustomer = getCustomerById(id);
        CustomerEntity updatedCustomer = customerMapper.apply(customerDto);

        BeanUtils.copyProperties(updatedCustomer, existingCustomer, getNullPropertyNames(updatedCustomer));
        CustomerEntity cs = customerRepository.save(existingCustomer);
        cs.setAccounts(null);
        return cs;
    }

    private void doesCustomerExists(Long id) {
        if(!customerRepository.existsById(id)){
            throw new ResourceNotFoundException("Customer Not Found");
        }
    }
}
