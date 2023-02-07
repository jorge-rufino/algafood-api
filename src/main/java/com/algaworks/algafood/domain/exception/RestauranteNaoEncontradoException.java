package com.algaworks.algafood.domain.exception;

//Quando esta exceção for lançada, vai devolver o Status "404 not found" pois foi herdado o Status 

public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException{
	private static final long serialVersionUID = 1L;

	public RestauranteNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

	public RestauranteNaoEncontradoException(Long id) {
		this(String.format("Restaurante de ID %d não existe!", id));
	}
}
