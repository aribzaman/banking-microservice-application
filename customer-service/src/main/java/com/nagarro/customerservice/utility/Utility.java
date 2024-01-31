package com.nagarro.customerservice.utility;

import com.nagarro.customerservice.entity.AccountEntity;
import com.nagarro.customerservice.entity.CustomerEntity;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.*;

public class Utility {

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        Set<String> propertyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor descriptor : src.getPropertyDescriptors()) {
            if (src.getPropertyValue(descriptor.getName()) == null) {
                propertyNames.add(descriptor.getName());
            }
        }
        return propertyNames.toArray(new String[propertyNames.size()]);
    }

    public static void populateCustomerAccounts(List<CustomerEntity> customers, List<AccountEntity> accounts) {
        Map<Long, List<AccountEntity>> accountsMap = new HashMap<>();
        for (AccountEntity account : accounts) {
            accountsMap.computeIfAbsent(account.getCustomerId(), k -> new ArrayList<>()).add(account);
        }

        for (CustomerEntity customer : customers) {
            List<AccountEntity> customerAccounts = accountsMap.get(customer.getId());
            if (customerAccounts != null) {
                customer.setAccounts(customerAccounts);
            } else {
                customer.setAccounts(Collections.emptyList());
            }
        }
    }
}
