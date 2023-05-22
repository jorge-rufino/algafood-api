package com.algaworks.algafood.api.v1.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "usuarios")
@Getter
@Setter
public class UsuarioDto extends RepresentationModel<UsuarioDto>{
	
	@Schema(example = "1")
	private Long id;
	
	@Schema(example = "Jorge")
	private String nome;
	
	@Schema(example = "jorge@algafood.com")
	private String email;	
}
