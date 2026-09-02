package com.marcio.newsletter_api.repositories;

import com.marcio.newsletter_api.domain.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
}
