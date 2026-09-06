    package com.marcio.newsletter_api.services;

    import com.marcio.newsletter_api.domain.Subscriber;
    import com.marcio.newsletter_api.repositories.SubscriberRepository;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class SubscriberService {
        private final SubscriberRepository subscriberRepository;

        public SubscriberService(SubscriberRepository subscriberRepository) {
            this.subscriberRepository = subscriberRepository;
        }

        public Subscriber saveSubscriber(Subscriber subscriber) {
                 return subscriberRepository.save(subscriber);
        }

        public List<Subscriber> findAllSubscribers() {
            return subscriberRepository.findAll();
        }
    }
