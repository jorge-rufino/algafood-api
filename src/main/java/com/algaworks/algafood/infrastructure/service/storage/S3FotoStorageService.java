package com.algaworks.algafood.infrastructure.service.storage;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.services.FotoStorageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3FotoStorageService implements FotoStorageService{

	@Autowired
	private AmazonS3 amazonS3;
	
	@Autowired
	private StorageProperties storageProperties;
	
	@Override
	public void armazenarFoto(NovaFoto novaFoto) {
		try {
			String caminhoArquivo = getCaminhoArquivo(novaFoto.getNomeArquivo());
						
			var objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(novaFoto.getContentType()); //Passamos o contentType para abrir a foto no navegador
			
//		Requisição que será enviada para Amazon
			var putObjectRequest = new PutObjectRequest(
					storageProperties.getS3().getBucket(), 
					caminhoArquivo, 
					novaFoto.getInputStream(), 
					objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead); //Permite o acesso a foto pois por padrão é negado.
			
			amazonS3.putObject(putObjectRequest);
			
		} catch (Exception e) {
			throw new StorageException("Não foi possíve enviar arquivo para AmazonS3", e);
		}
	}

//	Retorna o caminho do diretorio/bucket mais o nome da foto
	private String getCaminhoArquivo(String nomeArquivo) {		
		return String.format("%s/%s", storageProperties.getS3().getDiretorioFotos(),nomeArquivo);
	}

	@Override
	public void removerFoto(String nomeArquivo) {
		
	}

	@Override
	public InputStream recuperarFoto(String nomeArquivo) {
		
		return null;
	}

}
