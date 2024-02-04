package com.nagarro.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nagarro.accountservice.dto.CustomerDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name= "Account")
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Account_number", nullable = false, unique = true)
    @NotNull
    private Long accountNumber;

    @Column(nullable = false, name = "IFSC_Code")
    @NotNull
    private String ifscCode;

    @Column(nullable = false)
    @NotNull
    private String branch;

    @Column(nullable = false)
    @NotNull
    private String city;

    @Column(nullable = false)
    @NotNull
    private Long customerId;

    @Column(nullable = false)
    @NotNull
    private double balance=0.0;

    @Transient
    private CustomerDto customer;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    public AccountEntity(Long accountNumber, String ifscCode, String branch, String city, Long customerId, double balance) {
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.branch = branch;
        this.city = city;
        this.customerId = customerId;
        this.balance = balance;
    }
}
