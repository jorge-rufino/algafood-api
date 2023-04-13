package com.algaworks.algafood.api.v1.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FotoProdutoDto extends RepresentationModel<FotoProdutoDto>{

	private String nomeArquivo;
	private String descricao;
	private String contentType;
	private Long tamanho;
}
