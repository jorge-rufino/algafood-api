package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Quando esta exceção for lançada, vai devolver o Status "409 Conflict"

@ResponseStatus(HttpStatus.CONFLICT)
public class EntidadeEmUsoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public EntidadeEmUsoException(String message) {
		super(message);
	}
}
