package com.neoaplicacoes.customer_api.model.dto.response;

public record AddressResponseDTO(
        Long id,
        String cep,
        String number,
        String complement,
        String street,
        String neighborhood,
        String city,
        String state
) {}
