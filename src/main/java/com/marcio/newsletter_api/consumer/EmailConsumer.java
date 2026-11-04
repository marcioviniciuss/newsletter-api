package com.marcio.newsletter_api.consumer;

import com.marcio.newsletter_api.dtos.events.NewSubscriberEventDTO;
import com.marcio.newsletter_api.services.MailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    private final MailService mailService;

    public EmailConsumer(MailService mailService) {
        this.mailService = mailService;
    }

    @RabbitListener(queues = "email-confirmation.queue")
    public void consumeEmailConfirmation(NewSubscriberEventDTO newSubscriberEventDTO) {
        mailService.sendWelcomeEmail(newSubscriberEventDTO.email(), newSubscriberEventDTO.nome());
    }
}
