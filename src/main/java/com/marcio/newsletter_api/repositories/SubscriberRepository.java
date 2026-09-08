package com.marcio.newsletter_api.repositories;

import com.marcio.newsletter_api.domain.Subscriber;
import com.marcio.newsletter_api.dtos.SubscriberResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    Boolean existsByEmail(String email);
    Subscriber findByEmail(String email);
}
