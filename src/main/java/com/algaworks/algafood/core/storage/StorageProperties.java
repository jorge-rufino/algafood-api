package com.algaworks.algafood.core.storage;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("algafood.storage")	//Chave do application.properties
public class StorageProperties {
	
//	Para alterar o valor dos atributos, é no "application.properties"
	
	private Local local = new Local();
	private S3 s3 = new S3();
	private TipoStorage tipo = TipoStorage.LOCAL;	//Por padrão o tipo de storage é local. 
			
	public enum TipoStorage {
		LOCAL,S3
	}
	
	@Getter
	@Setter
	public class Local{
		
		private Path diretorioFotos;
	}

	@Getter
	@Setter
	public class S3{
		
//		As chaves de acesso estão configuradas diretamente na IDE
		private String idChaveAcesso;
		private String idChaveAcessoSecreta;
		private String bucket;
		private String regiao;
		private String diretorioFotos;
	}
	
}
