package com.algaworks.algafood.api.v2.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "cozinhas")
@Setter
@Getter
public class CozinhaDtoV2 extends RepresentationModel<CozinhaDtoV2>{
	
	private Long idCozinha;
	private String nomeCozinha;
}
