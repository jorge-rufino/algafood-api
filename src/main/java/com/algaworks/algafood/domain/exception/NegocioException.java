package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Quando esta exceção for lançada, vai devolver o Status "409 Conflict"

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NegocioException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public NegocioException(String message) {
		super(message);
	}
}
