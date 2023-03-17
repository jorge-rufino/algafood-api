package com.algaworks.algafood.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile>{

//	Representa um tamanho de dados. Faremos a conversão com ele
	private DataSize maxSize;
	
	@Override
	public void initialize(FileSize constraintAnnotation) {
//		Faz a conversão da String recebida como parametro para "bytes"	
		this.maxSize =DataSize.parse(constraintAnnotation.max());
	}
	
	@Override
	public boolean isValid(MultipartFile arquivo, ConstraintValidatorContext context) {
		
//		Se o tamanho maximo do arquivo para upload for menor ou igual ao tamanho passado como parametro
//		"value.getSize()" retorna o valor em bytes portanto devemos converter tb o "maxSize"
		
		return arquivo == null || arquivo.getSize() <= this.maxSize.toBytes();
	}

}
