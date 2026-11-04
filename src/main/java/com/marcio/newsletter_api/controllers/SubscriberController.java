package com.marcio.newsletter_api.controllers;

import com.marcio.newsletter_api.domain.Subscriber;
import com.marcio.newsletter_api.dtos.MessageResponseDTO;
import com.marcio.newsletter_api.dtos.events.TestSubscribedEventDTO;
import com.marcio.newsletter_api.dtos.subscribers.SubscriberRequestDTO;
import com.marcio.newsletter_api.dtos.subscribers.SubscriberResponseDTO;
import com.marcio.newsletter_api.services.MailService;
import com.marcio.newsletter_api.services.SubscriberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/subscribers")
@CrossOrigin(origins = "*")
public class SubscriberController {

    private final SubscriberService subscriberService;
    private final MailService mailService;

    public SubscriberController(SubscriberService subscriberService, MailService mailService) {
        this.subscriberService = subscriberService;
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> saveSubscriber(@Valid @RequestBody SubscriberRequestDTO subscriberRequestDTO) {
        boolean isNewElement = subscriberService.saveSubscriber(subscriberRequestDTO);
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO(
                "Cadastro realizado.",
                "O solicitação de cadastro foi feita com sucesso," +
                        " você receberá um e-mail confirmando a inscrição."
        );

        if (!isNewElement) {
            log.warn("Tentativa de cadastro com e-mail já existente: {}", subscriberRequestDTO.email());
        } else {
            log.info("E-mail cadastrado com sucesso.");
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(messageResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<SubscriberResponseDTO>> findAllSubscribers() {
        List<Subscriber> subscribers = subscriberService.findAllSubscribers();
        return ResponseEntity.status(HttpStatus.OK).body(subscribers.stream().map(SubscriberResponseDTO::new).toList());
    }

    @PostMapping("/test")
    public ResponseEntity<String> sendTestEmail(@RequestBody TestSubscribedEventDTO testSubscribedEventDTO) {
        mailService.sendWelcomeEmail(testSubscribedEventDTO.email(), testSubscribedEventDTO.nome());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("E-mail de confirmação enviado com sucesso.");
    }
}
