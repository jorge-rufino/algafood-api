package com.algaworks.algafood.domain.exception;

//Quando esta exceção for lançada, vai devolver o Status "404 not found" pois foi herdado o Status 

public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException{
	private static final long serialVersionUID = 1L;

	public EstadoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

	public EstadoNaoEncontradoException(Long id) {
		this(String.format("Estado de ID %d não existe!", id));
	}
}
