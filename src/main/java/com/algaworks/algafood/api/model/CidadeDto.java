package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

//Altera a representação da coleção para "cidades" no JSON pois o "Hateoas" utiliza o nome da classe como padrão(collectionCidadeDto)
@Relation(collectionRelation = "cidades")
@Getter
@Setter
public class CidadeDto extends RepresentationModel<CidadeDto>{
	
	private Long id;
	private String nome;
	private EstadoDto estado;
}
