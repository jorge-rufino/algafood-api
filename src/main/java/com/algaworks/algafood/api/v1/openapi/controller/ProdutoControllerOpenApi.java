package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.ProdutoDto;
import com.algaworks.algafood.api.v1.model.input.ProdutoInputDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_oauth")
public interface ProdutoControllerOpenApi {

	CollectionModel<ProdutoDto> listar(Long restauranteId, Boolean incluirInativos);

	ProdutoDto buscarPorId(Long restauranteId, Long produtoId);

	ProdutoDto adicionar(Long restauranteId, ProdutoInputDto produtoInput);

	ProdutoDto atualizar(Long restauranteId, Long produtoId, ProdutoInputDto produtoInput);

}