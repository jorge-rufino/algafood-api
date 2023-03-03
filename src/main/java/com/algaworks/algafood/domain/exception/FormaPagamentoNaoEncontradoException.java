package com.algaworks.algafood.domain.exception;

public class FormaPagamentoNaoEncontradoException extends EntidadeNaoEncontradaException{
	private static final long serialVersionUID = 1L;

	public FormaPagamentoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

	public FormaPagamentoNaoEncontradoException(Long id) {
		this(String.format("Forma de Pagamento de ID %d não existe!", id));
	}
}
