package com.algaworks.algafood.infrastructure.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.algaworks.algafood.domain.services.EnvioEmailService.Mensagem;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class ProcessadorEmailTemplate {

	@Autowired
	private Configuration freeMarkerConfig;
			
	protected String processarTemplate(Mensagem mensagem) {
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
