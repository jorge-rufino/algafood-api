package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeResumoDto extends RepresentationModel<CidadeResumoDto>{
	
	private Long id;
	private String nome;
	private String estado;
}
