package com.algaworks.algafood.api.v1.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "produtos")
@Setter
@Getter
public class ProdutoDto extends RepresentationModel<ProdutoDto>{

	@Schema(example = "1")
	private Long id;
	
	@Schema(example = "Espetinho de cupim")
	private String nome;
	
	@Schema(example = "Acompanha arroz, feijão e farofa")
	private String descricao;
	
	@Schema(example = "15.00")
	private BigDecimal preco;
	
	@Schema(example = "true")
	private Boolean ativo;	
}
