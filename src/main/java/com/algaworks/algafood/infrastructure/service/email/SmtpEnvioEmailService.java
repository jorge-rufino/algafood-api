package com.algaworks.algafood.infrastructure.service.email;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.services.EnvioEmailService;

@Service
public class SmtpEnvioEmailService implements EnvioEmailService{

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailProperties emailProperties;
	
	@Override
	public void enviar(Mensagem mensagem) {
		try {
//		Objeto que será enviado	
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
//		Facilitador para criar a mensagem que será enviada. Setamos com UTF-8 para evitar erro de codificação de mensagens
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(emailProperties.getRemetente());
			helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));	//Convertemos o Set em um Array de destinatarios
			helper.setSubject(mensagem.getAssunto());
			helper.setText(mensagem.getCorpo(), true);	//"True" é para mandar a mensagem em HTML. Senão seria enviado o texto puro.
			
			mailSender.send(mimeMessage);
			
		} catch (Exception e) {
			throw new EmailException("Não foi possível enviar e-mail.", e);
		}
		
	}

}
