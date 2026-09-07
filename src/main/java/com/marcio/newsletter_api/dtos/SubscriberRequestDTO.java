package com.marcio.newsletter_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SubscriberRequestDTO(
        @NotBlank(message = "O nome do usuário não pode ser Vazio ou Nulo.")
        String nome,

        @NotBlank(message = "O campo e-mail é obrigatório.")
        @Email(message = "O formato de e-mail é inválido.")
        String email) {
}
