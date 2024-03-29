package com.algaworks.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
	ACESSO_NEGADO("/acesso-negado", "Acesso negado"),
	ERRO_REQUISICAO("/erro-de-requisicao","Erro de Requisição."),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado","Recurso não encontrado."),
	ENTIDADE_EM_USO("/entidade-em-uso","Entidade em uso."),
	ERRO_NEGOCIO("/erro-negocio","Violação de regra de negócio."),
	PARAMETRO_INVALIDO("/parametro-invalido","Parâmetro de URL inválido."),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de Sistema."),
	ERRO_VALIDACAO("dados-inválidos","Dados inválidos.");
	
	private String title;
	private String uri;
	
	ProblemType(String path, String title){
		this.uri = "https://algafood.com.br" + path;
		this.title = title;
	}
}
