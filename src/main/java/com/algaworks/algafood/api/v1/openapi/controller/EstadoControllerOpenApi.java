package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.EstadoDto;
import com.algaworks.algafood.api.v1.model.input.EstadoInputDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_oauth")
@Tag(name = "Estados")
public interface EstadoControllerOpenApi {

	@Operation(summary = "Lista os estados")
	CollectionModel<EstadoDto> listar();

	@Operation(summary = "Busca um estado por ID", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID do estado inválido", content = {@Content(schema = @Schema(ref = "Problema")) }),
			@ApiResponse(responseCode = "404", description = "Estado não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
	})
	EstadoDto buscarId(@Parameter(description = "ID de um estado", example = "1", required = true) Long estadoId);

	@Operation(summary = "Cadastra um estado", responses = {
			@ApiResponse(responseCode = "201", description = "Estado cadastrado")
	})
	EstadoDto adicionar(@RequestBody(description = "Representação de um novo estado", required = true) EstadoInputDto estadoInput);

	@Operation(summary = "Exclui um estado por ID", responses = {
			@ApiResponse(responseCode = "200", description = "Estado excluido com sucesso"),
			@ApiResponse(responseCode = "400", description = "ID do estado inválido", content = {@Content(schema = @Schema(ref = "Problema")) }),
			@ApiResponse(responseCode = "404", description = "Estado não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
	})
	void deletar(@Parameter(description = "ID de um estado", example = "1", required = true) Long estadoId);

	@Operation(summary = "Atualiza um estado por ID", responses = {
			@ApiResponse(responseCode = "200", description = "Estado atualizado"),
			@ApiResponse(responseCode = "404", description = "Estado não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
	})
	EstadoDto atualizar(@RequestBody(description = "Representação de um estado com os novos dados", required = true) EstadoInputDto estadoInput, 
			@Parameter(description = "ID de um estado", example = "1", required = true) Long estadoId);

}