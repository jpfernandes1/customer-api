package com.neoaplicacoes.customerapi.model.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CustomerRequestDTO(
    @NotBlank(message = "Nome é obrigatório") String name,
    @NotBlank(message = "Email é obrigatório") @Email(message = "Email deve ser válido")
        String email,
    @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
        String cpf,
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos") String phone,
    @NotNull(message = "Data de nascimento é obrigatória")
        @Past(message = "Data de nascimento deve ser no passado")
        LocalDate birthDate,
    @NotNull(message = "Endereço é obrigatório") AddressRequestDTO address) {}
