package com.algaworks.algafood.api.v1.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

//Altera a representação da coleção para "cidades" no JSON pois o "Hateoas" utiliza o nome da classe como padrão(collectionCidadeDto)
@Relation(collectionRelation = "cidades")
@Getter
@Setter
public class CidadeDto extends RepresentationModel<CidadeDto>{
	
	@Schema(example = "1")
	private Long id;
	
	@Schema(example = "Belém")
	private String nome;
	
	private EstadoDto estado;
}
