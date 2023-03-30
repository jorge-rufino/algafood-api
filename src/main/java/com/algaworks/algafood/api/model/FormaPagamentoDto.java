package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormaPagamentoDto extends RepresentationModel<FormaPagamentoDto>{
	
	private Long id;
	private String descricao;
}
