package com.marcio.newsletter_api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange newsletterExchange() {
        return new TopicExchange("newsletter.events");
    }

    @Bean
    public Queue emailQueue() {
        return new Queue("email-confirmation.queue", true);
    }

    // 3. CRIANDO O BINDING (O Filtro / A Regra de Roteamento)
    // Aqui nós ligamos a Fila na Exchange e dizemos qual "etiqueta" (Routing Key) ela aceita
    @Bean
    public Binding emailConfirmationBinding(Queue emailQueue, TopicExchange newsletterExchange) {
        return BindingBuilder
                .bind(emailQueue)          // Pegue a gaveta "email-confirmation.queue"...
                .to(newsletterExchange)              // ...conecte no roteador "newsletter.events"...
                .with("email.confirmation");          // ...e SÓ deixe entrar mensagens com a etiqueta "email.confirmation"
    }

    @Bean
    public JacksonJsonMessageConverter conversorJson() {
        return new JacksonJsonMessageConverter();
    }
}
