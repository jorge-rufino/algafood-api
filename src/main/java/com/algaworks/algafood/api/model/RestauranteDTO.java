package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

//O JsonView infelizmente não funciona com o RepresentationModel, portanto vamos deixar de utiliza-lo

@Getter
@Setter
public class RestauranteDto extends RepresentationModel<RestauranteDto>{
	
//	@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNome.class})	
	private Long id;
			
//	@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNome.class})
	private String nome;
	
//	@JsonView(RestauranteView.Resumo.class)
	private BigDecimal taxaFrete;	
	
//	@JsonView(RestauranteView.Resumo.class)
	private CozinhaDto cozinha; 
	
	private EnderecoDto endereco;
	private Boolean ativo;
	private Boolean aberto;
	 
}
