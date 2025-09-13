package com.neoaplicacoes.customerapi.model.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(
    LocalDateTime timestamp, int status, String message, Map<String, String> errors) {}
