package com.algaworks.algafood.infrastructure.service.report;

public class ReporException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ReporException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReporException(String message) {
		super(message);
	}
}
