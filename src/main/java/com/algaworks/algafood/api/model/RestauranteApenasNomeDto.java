package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "restaurantes")
@Getter
@Setter
public class RestauranteApenasNomeDto extends RepresentationModel<RestauranteApenasNomeDto>{
	
	private Long id;
	private String nome;
}