package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.GrupoDto;
import com.algaworks.algafood.api.v1.model.input.GrupoInputDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_oauth")
public interface GrupoControllerOpenApi {

	CollectionModel<GrupoDto> listar();

	GrupoDto buscarPorId(Long id);

	GrupoDto adicionar(GrupoInputDto grupoInput);

	GrupoDto atualizar(Long id, GrupoInputDto grupoInput);

	void deletar(Long id);

}