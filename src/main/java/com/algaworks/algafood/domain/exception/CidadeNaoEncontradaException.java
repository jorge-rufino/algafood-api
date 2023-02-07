package com.algaworks.algafood.domain.exception;

//Quando esta exceção for lançada, vai devolver o Status "404 not found" pois foi herdado o Status 

public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException{
	private static final long serialVersionUID = 1L;

	public CidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public CidadeNaoEncontradaException(Long id) {
		this(String.format("Cidade de ID %d não existe!", id));
	}
}
