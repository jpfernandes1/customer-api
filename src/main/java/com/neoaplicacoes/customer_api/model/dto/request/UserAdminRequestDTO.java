package com.neoaplicacoes.customer_api.model.dto.request;

public record UserAdminRequestDTO(
        String role,
        Boolean active
) {}