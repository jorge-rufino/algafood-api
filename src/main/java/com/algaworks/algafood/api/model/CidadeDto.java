package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeDto extends RepresentationModel<CidadeDto>{
	
	private Long id;
	private String nome;
	private EstadoDto estado;
}
