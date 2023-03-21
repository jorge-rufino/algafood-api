package com.algaworks.algafood.core.email;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Validated
@Setter
@Getter
@Component
@ConfigurationProperties("algafood.email")
public class EmailProperties {
	
	@NotNull
	private String remetente;	//Se essa variavel nao estiver definida no application.properties, nem vai inicializar a API
		
	private Implementacao implementacao = Implementacao.FAKE;			//"fake" para usar email fake(é o padrao) ou "smtp" para utilizar email real
	
	public enum Implementacao{
		FAKE,SMTP
	}
}
