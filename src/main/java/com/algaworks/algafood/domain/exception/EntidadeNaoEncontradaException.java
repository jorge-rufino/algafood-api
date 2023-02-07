package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Quando esta exceção for lançada, vai devolver o Status "404 not found"

@ResponseStatus(value = HttpStatus.NOT_FOUND) 	// reason = "Entidade não encontrada!")
public abstract class EntidadeNaoEncontradaException extends NegocioException{
	private static final long serialVersionUID = 1L;

	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

}
