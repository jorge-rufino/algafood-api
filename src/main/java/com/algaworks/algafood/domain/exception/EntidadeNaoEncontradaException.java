package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

//Quando esta exceção for lançada, vai devolver o Status "404 not found"

//@ResponseStatus(value = HttpStatus.NOT_FOUND) 	// reason = "Entidade não encontrada!")
public class EntidadeNaoEncontradaException extends ResponseStatusException{
	private static final long serialVersionUID = 1L;
			
	//Agora permite escolher qual "Status" queremos
	public EntidadeNaoEncontradaException(HttpStatus status, String mensagem) {
		super(status, mensagem);
	}

	//Por padrão caso não seja especificado o Status, ele será o "404 not Found"
	public EntidadeNaoEncontradaException(String mensagem) {
		this(HttpStatus.NOT_FOUND, mensagem);
	}
}
