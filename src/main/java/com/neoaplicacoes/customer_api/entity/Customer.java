package com.neoaplicacoes.customer_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "CPF must have exactly 11 digits")
    @Column(name = "cpf", length = 11, nullable = false, unique = true)
    private String cpf;

    private String phone;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Transient
    public Integer getAge() {
        if (this.birthDate == null) return null;
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }
}
