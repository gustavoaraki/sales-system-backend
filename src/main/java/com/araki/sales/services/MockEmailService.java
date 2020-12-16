package com.araki.sales.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService{

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(MockEmailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Simulando envio de Email...");
		LOG.info(msg.toString());
		LOG.info("Email enviado!");
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Simulando envio de Email HTML...");
		LOG.info(msg.toString());
		LOG.info("Email enviado!");
	}

}
