package com.algaworks.algafood.api.v1.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

//@JsonFilter("pedidoFilter")
@Relation(collectionRelation = "pedidos")
@Setter
@Getter
public class PedidoResumoDto extends RepresentationModel<PedidoResumoDto>{
	
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
	
	private RestauranteApenasNomeDto restaurante;
	private UsuarioDto cliente;	
	
}
