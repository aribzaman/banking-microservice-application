package com.nagarro.customerservice;

import com.nagarro.customerservice.dao.CustomerRepository;
import com.nagarro.customerservice.dto.CustomerMapper;
import com.nagarro.customerservice.service.implementation.CustomerServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

}
