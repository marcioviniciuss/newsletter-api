package com.marcio.newsletter_api.producer;

import com.marcio.newsletter_api.dtos.events.NewSubscriberEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    public EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmailConfirmation(NewSubscriberEventDTO newSubscriberEventDTO) {
        rabbitTemplate.convertAndSend(
                "newsletter.events",
                "email.confirmation",
                newSubscriberEventDTO  );
    }
}
