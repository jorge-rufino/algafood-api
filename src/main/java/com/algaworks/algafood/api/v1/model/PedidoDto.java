package com.algaworks.algafood.api.v1.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PedidoDto extends RepresentationModel<PedidoDto>{
	
	private String codigo;
	private BigDecimal subtotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	private String status;
	private OffsetDateTime dataCriacao;	
	private OffsetDateTime dataConfirmacao;
	private OffsetDateTime dataEntrega;
	private OffsetDateTime dataCancelamento;
	private RestauranteApenasNomeDto restaurante;
	private UsuarioDto cliente;
	private FormaPagamentoDto formaPagamento;
	private EnderecoDto enderecoEntrega;
	private List<ItemPedidoDto> itens;	
}
