    package com.marcio.newsletter_api.services;

    import com.marcio.newsletter_api.domain.Subscriber;
    import com.marcio.newsletter_api.dtos.events.NewSubscriberEventDTO;
    import com.marcio.newsletter_api.dtos.subscribers.SubscriberRequestDTO;
    import com.marcio.newsletter_api.producer.EmailProducer;
    import com.marcio.newsletter_api.repositories.SubscriberRepository;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.event.TransactionPhase;
    import org.springframework.transaction.event.TransactionalEventListener;

    import java.util.List;

    @Service
    public class SubscriberService {
        private final SubscriberRepository subscriberRepository;
        private final EmailProducer emailProducer;

        public SubscriberService(SubscriberRepository subscriberRepository, EmailProducer emailProducer) {
            this.subscriberRepository = subscriberRepository;
            this.emailProducer = emailProducer;
        }

        private Subscriber getNewSubscriber(Subscriber subscriber) {
            return subscriberRepository.findById(subscriber.getId())
                    .orElseGet(() -> new Subscriber(null, null));
        };

        private NewSubscriberEventDTO getNewSubscriberEvent(Subscriber subscriber) {
            return new NewSubscriberEventDTO(subscriber.getId(), subscriber.getNome(), subscriber.getEmail());
        }

        @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
        public boolean saveSubscriber(SubscriberRequestDTO subscriberRequestDTO) {

            if (subscriberRepository.existsByEmail(subscriberRequestDTO.email())) {
                return false;
            }

            Subscriber subscriber = new Subscriber(subscriberRequestDTO.nome(), subscriberRequestDTO.email());
            subscriberRepository.save(subscriber);

             subscriber = getNewSubscriber(subscriber);

            emailProducer.sendEmailConfirmation(getNewSubscriberEvent(subscriber));

            return true;
        }

        public List<Subscriber> findAllSubscribers() {
            return subscriberRepository.findAll();
        }
    }
