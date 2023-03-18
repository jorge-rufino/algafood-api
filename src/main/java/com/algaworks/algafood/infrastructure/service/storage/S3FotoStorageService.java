package com.algaworks.algafood.infrastructure.service.storage;

import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.services.FotoStorageService;

@Service
public class S3FotoStorageService implements FotoStorageService{

	@Override
	public void armazenarFoto(NovaFoto novafoto) {
		
	}

	@Override
	public void removerFoto(String nomeArquivo) {
		
	}

	@Override
	public InputStream recuperarFoto(String nomeArquivo) {
		
		return null;
	}

}
