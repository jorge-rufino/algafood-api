package com.algaworks.algafood.api.v2.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

//Altera a representação da coleção para "cidades" no JSON pois o "Hateoas" utiliza o nome da classe como padrão(collectionCidadeDto)
@Relation(collectionRelation = "cidades")
@Getter
@Setter
public class CidadeDtoV2 extends RepresentationModel<CidadeDtoV2>{
	
	private Long idCidade;
	private String nomeCidade;
	
	private Long idEstado;
	private String nomeEstado;
}
