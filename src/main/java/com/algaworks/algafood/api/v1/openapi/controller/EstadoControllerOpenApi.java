package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.EstadoDto;
import com.algaworks.algafood.api.v1.model.input.EstadoInputDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_oauth")
public interface EstadoControllerOpenApi {

	CollectionModel<EstadoDto> listar();

	EstadoDto buscarId(Long estadoId);

	EstadoDto adicionar(EstadoInputDto estadoInput);

	void deletar(Long estadoId);

	EstadoDto atualizar(EstadoInputDto estadoInput, Long estadoId);

}