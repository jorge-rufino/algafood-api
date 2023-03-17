package com.algaworks.algafood.domain.services;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

public interface FotoStorageService {

	void armazenar(NovaFoto novafoto);
	
	default String gerarNomeArquivo(String nomeOriginal) {
		return UUID.randomUUID().toString()+"_"+nomeOriginal;
	}
	
	@Builder
	@Getter
	class NovaFoto {
		
		private String nomeArquivo;
		private InputStream inputStream;
	}
}
