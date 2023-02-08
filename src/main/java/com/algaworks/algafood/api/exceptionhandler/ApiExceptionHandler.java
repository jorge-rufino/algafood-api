package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;

//Esta classe captura Exceptions de todos os controladores 

@RestControllerAdvice
public class ApiExceptionHandler {
	
//	Podemos capturar exceçoes de mais de uma classe, basta adicionar chaves e vírgula como argumentos do método
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handlerEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e){
		
//		Exemplo de uso de objeto com a annotation "Builder"
		Problema problema = Problema.builder()
				.dataHora(LocalDateTime.now())
				.mensagem(e.getMessage()).build();
				
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
	}
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handlerEntidadeEmUsoException(EntidadeEmUsoException e){
		
		Problema problema = Problema.builder()
				.dataHora(LocalDateTime.now())
				.mensagem(e.getMessage()).build();
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(problema);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handlerNegocioException(NegocioException e){
		
		Problema problema = Problema.builder()
				.dataHora(LocalDateTime.now())
				.mensagem(e.getMessage()).build();
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
	}
	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<?> handlerHttpMediaTypeNotSupportedException(){
		
		Problema problema = Problema.builder()
				.dataHora(LocalDateTime.now())
				.mensagem("O tipo de mídia não é suportado.").build();
		
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(problema);
	} 
}
