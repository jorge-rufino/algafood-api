package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.CidadeDto;
import com.algaworks.algafood.api.v1.model.input.CidadeInputDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_oauth")
@Tag(name = "Cidades")
public interface CidadeControllerOpenApi {

	@Operation(summary = "Lista as cidades")
	CollectionModel<CidadeDto> listar();

	@Operation(summary = "Busca uma cidade por ID", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID da cidade inválido", content = @Content(schema = @Schema(ref = "Problema"))),
			@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(ref = "Problema")))
			
		})
	CidadeDto buscarPorId(@Parameter(description = "ID de uma cidade", example = "1", required = true) Long cidadeId);

	@Operation(summary = "Cadastra uma nova cidade", description = "Cadastrar uma nova cidade necessita de um Estado e um nome válido")
	CidadeDto adicionar(@RequestBody(description = "Representação de uma nova cidade", required = true) CidadeInputDto cidadeInputDto);

	@Operation(summary = "Atualiza/Altera uma cidade por ID",responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID da cidade inválido", content = @Content(schema = @Schema(ref = "Problema"))),
			@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(ref = "Problema")))
			
		})
	CidadeDto atualizar(@Parameter(description = "ID de uma cidade", example = "1", required = true) Long cidadeId, 
			@RequestBody(description = "Representação de uma cidade com dados atualizados", required = true) CidadeInputDto cidadeInput);

	@Operation(summary = "Exclui uma cidade por ID", responses = {
			@ApiResponse(responseCode = "204"),
			@ApiResponse(responseCode = "400", description = "ID da cidade inválido", content = @Content(schema = @Schema(ref = "Problema"))),
			@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(ref = "Problema")))
			
		})
	void deletar(@Parameter(description = "ID de uma cidade", example = "1", required = true) Long cidadeId);

}