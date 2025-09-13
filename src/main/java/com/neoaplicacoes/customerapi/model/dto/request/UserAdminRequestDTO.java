package com.neoaplicacoes.customerapi.model.dto.request;

import com.neoaplicacoes.customerapi.model.enums.Role;
import jakarta.validation.constraints.NotNull;

public record UserAdminRequestDTO(
    @NotNull(message = "Role must not be null") Role role, Boolean active) {}
