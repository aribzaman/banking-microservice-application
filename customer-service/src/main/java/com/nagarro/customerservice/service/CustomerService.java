package com.nagarro.customerservice.service;

import com.nagarro.customerservice.dao.CustomerRepository;
import com.nagarro.customerservice.dto.CustomerDto;
import com.nagarro.customerservice.dto.CustomerMapper;
import com.nagarro.customerservice.entity.CustomerEntity;
import com.nagarro.customerservice.exception.ResourceNotFoundException;
import com.nagarro.customerservice.feign.AccountFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nagarro.customerservice.utility.Utility.getNullPropertyNames;

@Service
@AllArgsConstructor
public class CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    AccountFeignClient accountFeignClient;

    public List<CustomerDto> getAllCustomer() {
        List<CustomerEntity> allCustomers = customerRepository.findAll();
        List<CustomerDto> customerDtoList = allCustomers.stream().map(customerMapper::reverse).toList();
        return customerDtoList;
    }

    public CustomerDto getCustomerById(Long id) {
        doesCustomerExists(id);
        CustomerEntity customer = customerRepository.findById(id).get();
        CustomerDto customerDto = customerMapper.reverse(customer);
        return customerDto;
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        CustomerEntity customerEntity = customerMapper.apply(customerDto);
        //[FEATURE] can incorporate request to create account with details: (branch, city, ifsc, amount)
        //accountFeignClient.createCustomer(AccountEntity accountEntity);
        return customerMapper.reverse(customerRepository.save(customerEntity));
    }

    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        doesCustomerExists(id);
        CustomerEntity existingCustomerToBeUpdated = customerRepository.findById(id).get();
        CustomerEntity updatedValues = customerMapper.apply(customerDto);

        BeanUtils.copyProperties(updatedValues, existingCustomerToBeUpdated, getNullPropertyNames(updatedValues));

        return customerMapper.reverse(customerRepository.save(existingCustomerToBeUpdated));
    }

    public boolean verifyCustomer(Long customerId, String name, Long phoneNumber) {
        doesCustomerExists(customerId);
        return customerRepository.existsByIdAndName(customerId, name);
    }

    public ResponseEntity<?> deleteCustomer(Long id) {
        doesCustomerExists(id);
        accountFeignClient.deleteAllAccountsByCustomerId(id);
        customerRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void doesCustomerExists(Long id) {
        if(!customerRepository.existsById(id)){
            throw new ResourceNotFoundException("Customer Not Found");
        }
    }

}
