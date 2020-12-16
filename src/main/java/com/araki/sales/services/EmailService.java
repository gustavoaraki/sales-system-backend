package com.araki.sales.services;

import javax.mail.internet.MimeMessage;

import com.araki.sales.model.Cliente;
import org.springframework.mail.SimpleMailMessage;

import com.araki.sales.model.Pedido;

public interface EmailService {
	
	void sendOrderConfirmation(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHtmlEmail(Pedido obj);
	
	void sendHtmlEmail(MimeMessage msg);

    void sendNewPasswordEmail(Cliente cliente, String newPass);
}
