package com.algaworks.algafood.domain.exception;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException{
	private static final long serialVersionUID = 1L;

	public PedidoNaoEncontradoException(String mensagem) {
		super(String.format("Pedido de código %s não existe!", mensagem));
	}

	public PedidoNaoEncontradoException(Long id) {
		this(String.format("Pedido de ID %d não existe!", id));
    }
}
