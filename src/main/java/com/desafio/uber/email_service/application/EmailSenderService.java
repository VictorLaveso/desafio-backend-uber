package com.desafio.uber.email_service.application;

import com.desafio.uber.email_service.adapters.EmailSenderGateway;
import com.desafio.uber.email_service.core.EmailSenderUseCase;
import com.desafio.uber.email_service.core.exceptions.EmailServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*Serviço spring onde contém a lógica de negócio*/

@Service
public class EmailSenderService implements EmailSenderUseCase {

    private final EmailSenderGateway emailSenderGateway;

    @Autowired
    public EmailSenderService(EmailSenderGateway emailSenderGateway) {
        this.emailSenderGateway = emailSenderGateway;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            this.emailSenderGateway.sendEmail(to, subject, body);
        } catch (Exception e) {
            throw new EmailServiceException("Failed to send email to " + to, e);
        }
    }
}
