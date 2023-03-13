package com.algaworks.algafood.domain.repository.filter;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

//Filtros dos Pedidos

@Setter
@Getter
public class PedidoFilter {
	
	private Long clienteId;
	private Long restauranteId;
	
//	Estamos forçando a formatação para o formato ISO, incluindo o offset
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime dataCriacaoInicio;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private OffsetDateTime dataCriacaoFim;
}
