package com.nagarro.customerservice.service.implementation;

import com.nagarro.customerservice.dao.CustomerRepository;
import com.nagarro.customerservice.dto.CustomerDto;
import com.nagarro.customerservice.dto.CustomerMapper;
import com.nagarro.customerservice.entity.CustomerEntity;
import com.nagarro.customerservice.exception.DuplicateEntryException;
import com.nagarro.customerservice.exception.ResourceNotFoundException;
import com.nagarro.customerservice.feign.AccountFeignClient;
import com.nagarro.customerservice.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.nagarro.customerservice.utility.Utility.getNullPropertyNames;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    AccountFeignClient accountFeignClient;

    @Override
    public List<CustomerDto> getAllCustomer() {
        List<CustomerEntity> allCustomers = customerRepository.findAll();
        return allCustomers.stream().map(customerMapper::reverse).toList();
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        doesCustomerExists(id);
        CustomerEntity customer = customerRepository.findById(id).get();
        return customerMapper.reverse(customer);
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        CustomerEntity customerEntity = customerMapper.apply(customerDto);
        duplicateCheck(customerDto);
        //[FEATURE] [implemented in V2] can incorporate request to create account with account details: (branch, city, ifsc, amount)
        //accountFeignClient.createAccount(AccountEntity accountEntity);
        return customerMapper.reverse(customerRepository.save(customerEntity));
    }

    @Override
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto) {
        doesCustomerExists(customerId);
        duplicateCheck(customerDto);
        CustomerEntity existingCustomerToBeUpdated = customerRepository.findById(customerId).get();
        CustomerEntity updatedValues = customerMapper.apply(customerDto);

        BeanUtils.copyProperties(updatedValues, existingCustomerToBeUpdated, getNullPropertyNames(updatedValues));

        return customerMapper.reverse(customerRepository.save(existingCustomerToBeUpdated));
    }

    @Override
    public boolean verifyCustomer(Long customerId, String name, Long phoneNumber) {
        doesCustomerExists(customerId);
        return customerRepository.existsByIdAndNameAndPhoneNumber(customerId, name, phoneNumber);
    }

    @Override
    public void deleteCustomer(Long id) {
        doesCustomerExists(id);
        accountFeignClient.deleteAllAccountsByCustomerId(id);
        customerRepository.deleteById(id);
    }

    private void doesCustomerExists(Long id) {
        if(!customerRepository.existsById(id)){
            throw new ResourceNotFoundException("Customer Not Found");
        }
    }

    private void duplicateCheck(CustomerDto customerDto) {
        if(!Objects.isNull(customerDto.phoneNumber()) && customerRepository.existsByPhoneNumber(customerDto.phoneNumber())){
            throw new DuplicateEntryException(customerDto.phoneNumber().toString());
        }
        if(!Objects.isNull(customerDto.email()) && customerRepository.existsByEmail(customerDto.email())){
            throw new DuplicateEntryException(customerDto.email());
        }
    }

}
