package com.nagarro.customerservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@DynamicUpdate
@Table(name= "Customer")
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only letters and spaces")
    private String name;

    @Email(message = "Email must be a well-formed email address")
    @NotNull(message = "Email cannot be null")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Address cannot be blank")
    @Column(nullable = false)
    private String address;

    @CreationTimestamp
    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdat;

    @UpdateTimestamp
    @Column(name = "modifiedat", nullable = false)
    private LocalDateTime modifiedat;

    @Transient
    private List<AccountEntity> accounts;

    public CustomerEntity(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }
}
