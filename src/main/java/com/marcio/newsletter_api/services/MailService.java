package com.marcio.newsletter_api.services;

import com.marcio.newsletter_api.integrations.mail.EmailTemplateFactory;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    // Resgata o e-mail de envio do .env ou application.yml
    @Value("${newsletter.mail.sender-email}")
    private String senderEmail;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(String recipientEmail, String recipientName) {
        log.info("Iniciando despacho de e-mail de confirmação para: {}", recipientEmail);

        try {
            MimeMessage message = mailSender.createMimeMessage();

            // true = multipart (permite HTML e anexos)
            // UTF-8 = garante suporte a pontuações e acentos
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(senderEmail, "NewsNews");
            helper.setTo(recipientEmail);
            helper.setSubject("O pulso do mundo tech direto no seu e-mail ⚡");

            // Gera o HTML estilizado através da nossa factory
            String htmlContent = EmailTemplateFactory.buildConfirmationEmail(recipientName);

            // O segundo parâmetro 'true' sinaliza ao cliente que o corpo é HTML e não texto plano
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("E-mail transacional enviado com sucesso para: {}", recipientEmail);

        } catch (MessagingException e) {
            log.error("Falha ao construir o envelope MIME para o e-mail: {}", recipientEmail, e);
            throw new RuntimeException("Erro estrutural na montagem do e-mail", e);
        } catch (MailException | UnsupportedEncodingException e) {
            log.error("Falha de rede ou recusa de autenticação pelo Brevo para: {}", recipientEmail, e);
            throw new RuntimeException("Falha no transporte SMTP", e);
        }
    }
}