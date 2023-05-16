package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.algaworks.algafood.api.v1.model.PedidoDto;
import com.algaworks.algafood.api.v1.model.PedidoResumoDto;
import com.algaworks.algafood.api.v1.model.input.PedidoInputDto;
import com.algaworks.algafood.domain.filter.PedidoFilter;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_oauth")
public interface PedidoControllerOpenApi {

	//	Mesmo sem o @RequestParam o spring consegue fazer o databind corretamente dos filtros
	PagedModel<PedidoResumoDto> pesquisar(PedidoFilter filtro, Pageable pageable);

	PedidoDto buscarPorCodigo(String codigoPedido);

	PedidoDto emitir(PedidoInputDto pedidoInput);

}