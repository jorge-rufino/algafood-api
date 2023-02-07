package com.algaworks.algafood.domain.exception;

//Quando esta exceção for lançada, vai devolver o Status "404 not found" pois foi herdado o Status 

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException{
	private static final long serialVersionUID = 1L;

	public CozinhaNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public CozinhaNaoEncontradaException(Long id) {
		this(String.format("Cozinha de ID %d não existe!", id));
	}
}
