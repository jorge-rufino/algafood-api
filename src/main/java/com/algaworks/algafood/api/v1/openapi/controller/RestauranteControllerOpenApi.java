package com.algaworks.algafood.api.v1.openapi.controller;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import com.algaworks.algafood.api.v1.model.RestauranteApenasNomeDto;
import com.algaworks.algafood.api.v1.model.RestauranteBasicoDto;
import com.algaworks.algafood.api.v1.model.RestauranteDto;
import com.algaworks.algafood.api.v1.model.input.RestauranteInputDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_oauth")
@Tag(name = "Restaurantes")
public interface RestauranteControllerOpenApi {

	@Operation(summary = "Lista restaurantes", parameters = {
		@Parameter(
				name = "projecao", 
				description = "Nome da projecao", 
				example = "apenas-nome", 
				in = ParameterIn.QUERY, 
				required = false)	
	})
	ResponseEntity<CollectionModel<RestauranteBasicoDto>> listar(ServletWebRequest request);

	@Operation(hidden = true)
	CollectionModel<RestauranteApenasNomeDto> listarApenasNome();

	@Operation(summary = "Busca um restaurante por ID", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID do restaurante inválido", content = {@Content(schema = @Schema(ref = "Problema")) }),
			@ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) }),
	})
	RestauranteDto buscarId(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long id);

	@Operation(summary = "Cadastra um restaurante", responses = {@ApiResponse(responseCode = "201", description = "Restaurante cadastrado"),
	})
	RestauranteDto adicionar(@RequestBody(description = "Representação de um novo restaurante", required = true) RestauranteInputDto restauranteInput);

	@Operation(summary = "Atualiza um restaurante por ID", responses = {
			@ApiResponse(responseCode = "200", description = "Restaurante atualizado"),
			@ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) }),
	})
	RestauranteDto atualizar(
			@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId, 
			@RequestBody(description = "Representação de um restaurante com os novos dados", required = true) RestauranteInputDto restauranteInput);

	@Operation(summary = "Ativa um restaurante por ID", responses = {
			@ApiResponse(responseCode = "204", description = "Restaurante ativado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) }),
	})
	ResponseEntity<Void> ativar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

	@Operation(summary = "Desativa um restaurante por ID", responses = {
			@ApiResponse(responseCode = "204", description = "Restaurante desativa com sucesso"),
			@ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) }),
	})
	ResponseEntity<Void> inativar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);
	
	@Operation(summary = "Ativa múltiplos restaurantes", responses = {
			@ApiResponse(responseCode = "204", description = "Restaurantes ativados com sucesso"),
	})
	void ativarMultiplos(@RequestBody(description = "IDs de restaurantes", required = true) List<Long> restauranteIds);

	@Operation(summary = "Desativa múltiplos restaurantes", responses = {
			@ApiResponse(responseCode = "204", description = "Restaurantes desativados com sucesso"),
	})
	void inativarMultiplos(@RequestBody(description = "IDs de restaurantes", required = true) List<Long> restauranteIds);

	@Operation(summary = "Abre um restaurante por ID", responses = {
			@ApiResponse(responseCode = "204", description = "Restaurante aberto com sucesso"),
			@ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) }),
	})
	ResponseEntity<Void> abrirRestaurante(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

	@Operation(summary = "Fecha um restaurante por ID", responses = {
			@ApiResponse(responseCode = "204", description = "Restaurante fechado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) }),
	})
	ResponseEntity<Void> fecharRestaurante(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

	@Operation(summary = "Exclui um restaurante por ID", responses = {
			@ApiResponse(responseCode = "204", description = "Restaurante excluído com sucesso"),
			@ApiResponse(responseCode = "400", description = "ID do restaurante inválido", content = {@Content(schema = @Schema(ref = "Problema")) }),
			@ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) }),
	})
	void deletar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long id);
}