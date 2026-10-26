package com.marcio.newsletter_api.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.marcio.newsletter_api.domain.Subscriber;

@JsonPropertyOrder({ "id", "nome", "email" })
public record SubscriberResponseDTO(Long id, String nome, String email) {
    public SubscriberResponseDTO(Subscriber subscriber) {
        this(subscriber.getId(), subscriber.getNome(), subscriber.getEmail());
    }
}
