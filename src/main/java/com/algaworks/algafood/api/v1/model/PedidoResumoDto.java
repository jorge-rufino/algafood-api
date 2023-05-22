package com.algaworks.algafood.api.v1.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

//@JsonFilter("pedidoFilter")
@Relation(collectionRelation = "pedidos")
@Setter
@Getter
public class PedidoResumoDto extends RepresentationModel<PedidoResumoDto>{
	
	@Schema(example = "04813f77-79b5-11ec-9a17-0242ac1b0002")
	private String codigo;
	
	@Schema(example = "298.90")
	private BigDecimal subtotal;
	
	@Schema(example = "10.00")
	private BigDecimal taxaFrete;
	
	@Schema(example = "308.90")
	private BigDecimal valorTotal;
	
	@Schema(example = "CRIADO")
	private String status;
		
	@Schema(example = "2019-12-01T20:34:04Z")
	private OffsetDateTime dataCriacao;
	
//	Só inclui na representaçao se nao for "Null"
	
	@Schema(example = "2022-12-01T20:35:10Z")
	@JsonInclude(Include.NON_NULL)
	private OffsetDateTime dataConfirmacao;
	
	@Schema(example = "2022-12-01T20:35:00Z")
	@JsonInclude(Include.NON_NULL)
	private OffsetDateTime dataCancelamento;
	
	private RestauranteApenasNomeDto restaurante;
	private UsuarioDto cliente;	
	
}
