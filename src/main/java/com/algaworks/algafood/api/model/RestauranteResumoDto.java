package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestauranteResumoDto extends RepresentationModel<RestauranteResumoDto>{
	
	private Long id;
	private String nome;
}
