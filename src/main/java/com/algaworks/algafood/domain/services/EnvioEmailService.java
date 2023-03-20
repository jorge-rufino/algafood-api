package com.algaworks.algafood.domain.services;

import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

public interface EnvioEmailService {
	
	void enviar(Mensagem mensagem);
	
	@Getter
	@Builder
	class Mensagem {
		
//		Annotation faz com que quando passarmos somente um objeto/destianatario, não seja necessário instanciar a lista
//		Ele tambem transforma o nome da propriedade para o singular, ou seja, "destinatario"...e se passarmos um list volta para o plural
		@Singular
		private Set<String> destinatarios;
		
		@NonNull
		private String assunto;
		
		@NonNull
		private String corpo;
		
//		Objeto para ser usado no template
		
		@Singular("variavel")				//Nome da variavel caso tenha somente 1 objeto
		private Map<String, Object> variaveis;
	}

}
