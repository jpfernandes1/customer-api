package com.neoaplicacoes.customer_api.model.dto.request;

import com.neoaplicacoes.customer_api.model.enums.Role;
import jakarta.validation.constraints.Pattern;

public record UserAdminRequestDTO(
        @Pattern(regexp = "ROLE_USER|ROLE_ADMIN", message = "Role must be either ROLE_USER or ROLE_ADMIN")
        Role role,
        Boolean active
) {}
