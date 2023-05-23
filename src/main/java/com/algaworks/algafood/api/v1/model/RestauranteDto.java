package com.algaworks.algafood.api.v1.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

//O JsonView infelizmente não funciona com o RepresentationModel, portanto vamos deixar de utiliza-lo

@Getter
@Setter
public class RestauranteDto extends RepresentationModel<RestauranteDto>{

	@Schema(example = "1")
	private Long id;
			
	@Schema(example = "Thai Gourmet")
	private String nome;

	@Schema(example = "10.00")
	private BigDecimal taxaFrete;	
	
	private CozinhaDto cozinha; 
	
	private EnderecoDto endereco;
	private Boolean ativo;
	private Boolean aberto;
	 
}
