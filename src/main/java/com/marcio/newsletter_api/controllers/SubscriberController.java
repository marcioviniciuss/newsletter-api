package com.marcio.newsletter_api.controllers;

import com.marcio.newsletter_api.domain.Subscriber;
import com.marcio.newsletter_api.dtos.SubscriberRequestDTO;
import com.marcio.newsletter_api.dtos.SubscriberResponseDTO;
import com.marcio.newsletter_api.services.SubscriberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping
    public ResponseEntity<SubscriberResponseDTO> saveSubscriber(@Valid @RequestBody SubscriberRequestDTO subscriberRequestDTO) {
        SubscriberResponseDTO responseSubscriber = subscriberService.saveSubscriber(subscriberRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseSubscriber);
    }

    @GetMapping
    public ResponseEntity<List<SubscriberResponseDTO>> getAllSubscribers() {
        List<Subscriber> subscribers = subscriberService.findAllSubscribers();
        List<SubscriberResponseDTO> subscriberResponseDTOS = new ArrayList<>();
        for (Subscriber s : subscribers) {
            subscriberResponseDTOS.add(new SubscriberResponseDTO(s));
        }
        return ResponseEntity.status(HttpStatus.OK).body(subscriberResponseDTOS);
    }
}
