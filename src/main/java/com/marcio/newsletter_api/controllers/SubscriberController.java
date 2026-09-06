package com.marcio.newsletter_api.controllers;

import com.marcio.newsletter_api.domain.Subscriber;
import com.marcio.newsletter_api.services.SubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping
    public ResponseEntity<Subscriber> saveSubscriber(@RequestBody Subscriber subscriber) {
        Subscriber responseSubscriber = subscriberService.saveSubscriber(subscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseSubscriber);
    }

    @GetMapping
    public ResponseEntity<List<Subscriber>> getAllSubscribers() {
        List<Subscriber> subscribers = subscriberService.findAllSubscribers();
        return ResponseEntity.status(HttpStatus.OK).body(subscribers);
    }
}
