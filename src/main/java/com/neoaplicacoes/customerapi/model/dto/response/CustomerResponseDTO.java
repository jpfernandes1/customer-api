package com.neoaplicacoes.customerapi.model.dto.response;

import java.time.LocalDate;

public record CustomerResponseDTO(
    Long id,
    String name,
    String email,
    String cpf,
    String phone,
    LocalDate birthDate,
    Integer age, // already calculed on service via getAge() of the entity
    AddressResponseDTO address) {}
