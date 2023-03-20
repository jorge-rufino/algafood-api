package com.algaworks.algafood.infrastructure.service.email;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.services.EnvioEmailService;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class SmtpEnvioEmailService implements EnvioEmailService{

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailProperties emailProperties;
	
	@Autowired
	private Configuration freeMarkerConfig;
	
	@Override
	public void enviar(Mensagem mensagem) {
		try {
			String corpo = processarTemplate(mensagem);
			
//		Objeto que será enviado	
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
//		Facilitador para criar a mensagem que será enviada. Setamos com UTF-8 para evitar erro de codificação de mensagens
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(emailProperties.getRemetente());
			helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));	//Convertemos o Set em um Array de destinatarios
			helper.setSubject(mensagem.getAssunto());
			helper.setText(corpo, true);	//"True" é para mandar a mensagem em HTML. Senão seria enviado o texto puro.
			
			mailSender.send(mimeMessage);
			
		} catch (Exception e) {
			throw new EmailException("Não foi possível enviar e-mail.", e);
		}
	}

	private String processarTemplate(Mensagem mensagem) {
		try {
//			Carrega o HTML
			Template template = freeMarkerConfig.getTemplate(mensagem.getCorpo());
			
//			Processa fazendo a junçao do HTML com o Objeto e retorna como String o html transformado
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, mensagem.getVariaveis());
			
		} catch (Exception e) {
			throw new EmailException("Não foi possível montar o template do email.", e);
		}
	}
}
