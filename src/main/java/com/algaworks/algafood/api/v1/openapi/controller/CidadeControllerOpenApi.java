package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.CidadeDto;
import com.algaworks.algafood.api.v1.model.input.CidadeInputDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_oauth")
@Tag(name = "Cidades")
public interface CidadeControllerOpenApi {

	@Operation(summary = "Lista as cidades")
	CollectionModel<CidadeDto> listar();

	@Operation(summary = "Busca uma cidade por ID")
	CidadeDto buscarPorId(Long cidadeId);

	@Operation(summary = "Cadastra uma nova cidade", description = "Cadastrar uma nova cidade necessita de um Estado e um nome válido")
	CidadeDto adicionar(CidadeInputDto cidadeInputDto);

	@Operation(summary = "Atualiza/Altera uma cidade por ID")
	CidadeDto atualizar(Long id, CidadeInputDto cidadeInput);

	@Operation(summary = "Exclui uma cidade por ID")
	void deletar(Long cidadeId);

}