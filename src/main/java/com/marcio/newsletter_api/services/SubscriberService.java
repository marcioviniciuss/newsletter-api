    package com.marcio.newsletter_api.services;

    import com.marcio.newsletter_api.domain.Subscriber;
    import com.marcio.newsletter_api.dtos.SubscriberRequestDTO;
    import com.marcio.newsletter_api.dtos.SubscriberResponseDTO;
    import com.marcio.newsletter_api.repositories.SubscriberRepository;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class SubscriberService {
        private final SubscriberRepository subscriberRepository;

        public SubscriberService(SubscriberRepository subscriberRepository) {
            this.subscriberRepository = subscriberRepository;
        }

        public SubscriberResponseDTO saveSubscriber(SubscriberRequestDTO subscriberRequestDTO) {
            Subscriber subscriber = new Subscriber(subscriberRequestDTO.nome(), subscriberRequestDTO.email());
            subscriberRepository.save(subscriber);
            SubscriberResponseDTO savedSubscriber = new SubscriberResponseDTO(subscriber);
            return savedSubscriber;
        }

        public List<Subscriber> findAllSubscribers() {
            return subscriberRepository.findAll();
        }
    }
