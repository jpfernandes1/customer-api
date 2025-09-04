package com.neoaplicacoes.customer_api.model.dto.request;

import com.neoaplicacoes.customer_api.model.enums.Role;
import jakarta.validation.constraints.NotNull;

public record UserAdminRequestDTO(
        @NotNull(message = "Role must not be null")
        Role role,
        Boolean active
) {}
