package com.algaworks.algafood.infrastructure.service.storage;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.exception.FotoProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.services.FotoStorageService;

public class LocalFotoStorageService implements FotoStorageService{

	@Autowired
	private StorageProperties storageProperties;
	
	@Override
	public void armazenarFoto(NovaFoto novaFoto) {
		try {
//			Local onde será salva a foto, mais o nome do arquivo
			Path localStoragePath = getArquivoPath(novaFoto.getNomeArquivo());	
			
//			Path localStoragePath = Path.of(diretorioFotos, novaFoto.getNomeArquivo());
			
//			Salva a foto no local definido
			FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(localStoragePath));
			
		} catch (Exception e) {
			throw new StorageException("Não foi possível armazenar o arquivo.", e);
		}
	}
	
	@Override
	public void removerFoto(String nomeArquivo) {
		try {			
			Path arquivoPath = getArquivoPath(nomeArquivo);
			
			Files.deleteIfExists(arquivoPath);
			
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir o arquivo.",e);
		}
	}
	
	private Path getArquivoPath(String nomeArquivo) {
		Path diretorioFotos = storageProperties.getLocal().getDiretorioFotos();
		
		return diretorioFotos.resolve(Path.of(nomeArquivo));
	}

	@Override
	public FotoRecuperada recuperarFoto(String nomeArquivo) {
		try {
	        Path arquivoPath = getArquivoPath(nomeArquivo);
	        
//	        Verifica se a foto existe no storage
	        if(Files.notExists(arquivoPath)) {
	        	throw new FotoProdutoNaoEncontradoException("Foto não encontrada no storage.");
	        }
	        
	        FotoRecuperada fotoRecuperada = FotoRecuperada.builder()
	        		.inputStream(Files.newInputStream(arquivoPath))
	        		.build();

	        return fotoRecuperada;
	    } catch (FotoProdutoNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new StorageException("Não foi possível recuperar arquivo.", e);
        }
	}

}
