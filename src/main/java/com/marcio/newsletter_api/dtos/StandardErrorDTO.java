package com.marcio.newsletter_api.dtos;

import java.time.LocalDateTime;

public record StandardErrorDTO(
        LocalDateTime dataHora,
        Integer statusHTTP,
        String tituloErro,
        String detalhes
) {
}
