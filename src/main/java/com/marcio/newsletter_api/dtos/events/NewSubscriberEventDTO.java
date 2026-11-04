package com.marcio.newsletter_api.dtos.events;

public record NewSubscriberEventDTO (
        Long id,
        String nome,
        String email
) {
}
