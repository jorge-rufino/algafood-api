package com.algaworks.algafood.infrastructure.service.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.algaworks.algafood.core.email.EmailProperties;

public class SandboxEnvioEmailService extends SmtpEnvioEmailService {

	@Autowired
	private EmailProperties emailProperties;
	
	@Override
	public MimeMessage createMimeMessage(Mensagem mensagem) throws MessagingException {
		MimeMessage mimeMessage = super.createMimeMessage(mensagem);
		
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
		helper.setSubject(mensagem.getAssunto() + " => [SANDBOX]");
		helper.setTo(emailProperties.getSandbox().getDestinatario());
		
		return mimeMessage;
	}

}