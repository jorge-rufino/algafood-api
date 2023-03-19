package com.algaworks.algafood.domain.services;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

public interface FotoStorageService {

	void armazenarFoto(NovaFoto novafoto);
	
	void removerFoto(String nomeArquivo);
	
//	Agora vamos retornar InputStream caso utilizem um storage Local, ou uma String que será a URL da foto caso em nuvem
	FotoRecuperada recuperarFoto(String nomeArquivo);
	
	default void substituir(String nomeArquivoAntigo, NovaFoto novaFoto) {
		this.armazenarFoto(novaFoto);
		
		if (nomeArquivoAntigo != null) {
			this.removerFoto(nomeArquivoAntigo);
		}
	}
	
	default String gerarNomeArquivo(String nomeOriginal) {
		return UUID.randomUUID().toString()+"_"+nomeOriginal;
	}
	
	@Builder
	@Getter
	class NovaFoto {
		
		private String nomeArquivo;
		private String contentType;
		private InputStream inputStream;
	}
	
	@Builder
	@Getter
	class FotoRecuperada {
		
		private InputStream inputStream;
		private String url;
		
		public boolean temUrl() {
			return url != null;
		}
		
		public boolean temInputStream() {
			return inputStream != null;
		}
	}
}
