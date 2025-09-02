package com.neoaplicacoes.customer_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressRequestDTO(
        @Pattern(regexp = "\\d{8}", message = "CEP deve ter 8 dígitos")
        String cep,

        @NotBlank(message = "Número é obrigatório")
        String number,

        String complement,

        @NotBlank(message = "Rua é obrigatória")
        String street,

        @NotBlank(message = "Bairro é obrigatório")
        String neighborhood,

        @NotBlank(message = "Cidade é obrigatória")
        String city,

        @NotBlank(message = "Estado é obrigatório")
        String state
) {}
