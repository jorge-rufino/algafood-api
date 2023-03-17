package com.algaworks.algafood.infrastructure.service.storage;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.domain.services.FotoStorageService;

@Service
public class LocalFotoStorageService implements FotoStorageService{

	@Value("${algafood.storage.local.diretorios-fotos}")
	private Path diretorioFotos;
	
//	@Value("${algafood.storage.local.diretorios-fotos}")
//	private String diretorioFotos;
	
	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
//			Local onde será salva a foto e o nome do arquivo
			Path localStoragePath = getArquivoPath(novaFoto.getNomeArquivo());	
			
//			Path localStoragePath = Path.of(diretorioFotos, novaFoto.getNomeArquivo());
			
//			Salva a foto no local definido
			FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(localStoragePath));
			
		} catch (Exception e) {
			throw new StorageException("Não foi possível armazenar o arquivo.", e);
		}
	}
	
	private Path getArquivoPath(String nomeArquivo) {
		return diretorioFotos.resolve(Path.of(nomeArquivo));
	}

}
