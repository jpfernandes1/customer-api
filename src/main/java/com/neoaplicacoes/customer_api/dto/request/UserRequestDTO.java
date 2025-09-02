package com.neoaplicacoes.customer_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password,

        String role
) {
    public UserRequestDTO {
        // Valor padrão para role se não informado
        if (role == null || role.isBlank()) {
            role = "ROLE_USER";
        }
    }
}