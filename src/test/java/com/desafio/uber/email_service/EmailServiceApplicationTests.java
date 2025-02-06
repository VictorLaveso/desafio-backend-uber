package com.desafio.uber.email_service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.desafio.uber.email_service.core.exceptions.EmailServiceException;
import com.desafio.uber.email_service.infrastructure.ses.SesEmailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmailServiceApplicationTests {

	private AmazonSimpleEmailService amazonSimpleEmailService;
	private SesEmailSender sesEmailSender;

	@BeforeEach
	void setUp() {
		amazonSimpleEmailService = mock(AmazonSimpleEmailService.class);
		sesEmailSender = new SesEmailSender(amazonSimpleEmailService);
	}

	@Test
	void SendEmailSuccessfully() {
		String to = "teste@teste.com";
		String subject = "Test Subject";
		String body = "Test Body";

		sesEmailSender.sendEmail(to, subject, body);

		ArgumentCaptor<SendEmailRequest> captor = ArgumentCaptor.forClass(SendEmailRequest.class);
		verify(amazonSimpleEmailService, times(1)).sendEmail(captor.capture());

		SendEmailRequest sentRequest = captor.getValue();
		assertEquals("victorhugolavezo@gmail.com", sentRequest.getSource());
		assertEquals(to, sentRequest.getDestination().getToAddresses().get(0));
		assertEquals(subject, sentRequest.getMessage().getSubject().getData());
		assertEquals(body, sentRequest.getMessage().getBody().getText().getData());
	}

	@Test
	void shouldHandleAmazonServiceException() {
		// Arrange
		String to = "destinatario@example.com";
		String subject = "Test Subject";
		String body = "Test Body";

		doThrow(new EmailServiceException("Failure while sending email"))
				.when(amazonSimpleEmailService)
				.sendEmail(any(SendEmailRequest.class));

		// Act & Assert
		Exception exception = assertThrows(EmailServiceException.class, () -> {
			sesEmailSender.sendEmail(to, subject, body);
		});

		assertEquals("Failure while sending email", exception.getMessage());
	}
}
