package com.algaworks.algafood.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

//@JsonFilter("pedidoFilter")
@Setter
@Getter
public class PedidoResumoDto {
	
	private String codigo;
	private BigDecimal subtotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	private String status;
		
	private OffsetDateTime dataCriacao;
	
//	Só inclui na representaçao se nao for "Null"
	
	@JsonInclude(Include.NON_NULL)
	private OffsetDateTime dataConfirmacao;
	
	@JsonInclude(Include.NON_NULL)
	private OffsetDateTime dataCancelamento;
	
	private RestauranteResumoDto restaurante;
	private UsuarioDto cliente;	
	
}
