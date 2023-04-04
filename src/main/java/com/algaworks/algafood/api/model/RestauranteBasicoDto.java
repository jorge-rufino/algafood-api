package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

//O JsonView infelizmente não funciona com o RepresentationModel, portanto vamos deixar de utiliza-lo

@Relation(collectionRelation = "restaurantes")
@Getter
@Setter
public class RestauranteBasicoDto extends RepresentationModel<RestauranteBasicoDto>{
	
	private Long id;
	private String nome;
	private BigDecimal taxaFrete;	
	private CozinhaDto cozinha; 
	 
}
