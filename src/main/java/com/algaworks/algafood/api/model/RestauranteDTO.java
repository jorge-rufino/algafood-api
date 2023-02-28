package com.algaworks.algafood.api.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestauranteDto {
	
	private Long id;
	private String nome;
	private BigDecimal taxaFrete;
	private CozinhaDto cozinha; 
	 
}
