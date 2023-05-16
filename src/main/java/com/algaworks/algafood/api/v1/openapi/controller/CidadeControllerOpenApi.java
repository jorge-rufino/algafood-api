package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.CidadeDto;
import com.algaworks.algafood.api.v1.model.input.CidadeInputDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_oauth")
@Tag(name = "Cidades")
public interface CidadeControllerOpenApi {

	CollectionModel<CidadeDto> listar();

	CidadeDto buscarPorId(Long cidadeId);

	CidadeDto adicionar(CidadeInputDto cidadeInputDto);

	CidadeDto atualizar(Long id, CidadeInputDto cidadeInput);

	void deletar(Long cidadeId);

}