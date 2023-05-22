package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.GrupoDto;
import com.algaworks.algafood.api.v1.model.input.GrupoInputDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_oauth")
@Tag(name = "Grupos")
public interface GrupoControllerOpenApi {

	@Operation(summary = "Lista os grupos")
	CollectionModel<GrupoDto> listar();

	@Operation(summary = "Busca um grupo por ID", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID do grupo inválido", content = @Content(schema = @Schema(ref = "Problema"))),
			@ApiResponse(responseCode = "404", description = "Grupo não encontrado", content = @Content(schema = @Schema(ref = "Problema")))			
		})
	GrupoDto buscarPorId(@Parameter(description = "ID de um grupo", example = "1", required = true) Long grupoId);

	@Operation(summary = "Cadastra um grupo")
	GrupoDto adicionar(@RequestBody(description = "Representação de um novo grupo", required = true) GrupoInputDto grupoInput);

	@Operation(summary = "Atualiza um grupo por ID", responses = {
			@ApiResponse(responseCode = "200", description = "Grupo atualizado"),
			@ApiResponse(responseCode = "400", description = "ID do grupo inválido", content = @Content(schema = @Schema(ref = "Problema"))),
			@ApiResponse(responseCode = "404", description = "Grupo não encontrado", content = @Content(schema = @Schema(ref = "Problema")))			
		})
	GrupoDto atualizar(
			@Parameter(description = "ID de um grupo", example = "1", required = true) Long id, 
			@RequestBody(description = "Representação de um grupo com os novos dados", required = true) GrupoInputDto grupoInput);

	@Operation(summary = "Exclui um grupo por ID", responses = {
			@ApiResponse(responseCode = "204", description = "Grupo excluído"),			
			@ApiResponse(responseCode = "404", description = "Grupo não encontrado", content = @Content(schema = @Schema(ref = "Problema")))			
		})
	void deletar(Long id);

}