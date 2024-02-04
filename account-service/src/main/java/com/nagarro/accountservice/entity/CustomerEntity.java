package com.nagarro.accountservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerEntity {

    private Long id;
    private String name;
    private String email;
    private String address;

}
