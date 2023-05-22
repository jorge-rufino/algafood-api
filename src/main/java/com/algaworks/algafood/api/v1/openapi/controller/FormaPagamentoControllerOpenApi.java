package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import com.algaworks.algafood.api.v1.model.FormaPagamentoDto;
import com.algaworks.algafood.api.v1.model.input.FormaPagamentoInputDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_oauth")
@Tag(name = "Formas de pagamento")
public interface FormaPagamentoControllerOpenApi {

	@Operation(summary = "Lista as formas de pagamento")
	ResponseEntity<CollectionModel<FormaPagamentoDto>> listar(@Parameter(hidden = true) ServletWebRequest request);

	@Operation(summary = "Busca uma forma de pagamento por ID", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID da forma de pagamento inválido", content = {@Content(schema = @Schema(ref = "Problema"))}),
			@ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada", content = {@Content(schema = @Schema(ref = "Problema"))})
	})
	ResponseEntity<FormaPagamentoDto> buscarId(
			@Parameter(description = "ID de uma forma de pagamento", example = "1", required = true) Long id, 
			@Parameter(hidden = true) ServletWebRequest request);

	@Operation(summary = "Cadastra uma forma de pagamento", responses = {
			@ApiResponse(responseCode = "201", description = "Forma de pagamento cadastrada")})
	FormaPagamentoDto adicionar(
			@RequestBody(description = "Representação de uma nova forma de pagamento", required = true) FormaPagamentoInputDto formaPagamentoInput);

	@Operation(summary = "Atualiza uma forma de pagamento por ID", responses = {
			@ApiResponse(responseCode = "200", description = "Forma de pagamento atualizada"),
			@ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada", content = {@Content(schema = @Schema(ref = "Problema")) })
	})
	FormaPagamentoDto atualizar(
			@Parameter(description = "ID de uma forma de pagamento", example = "1", required = true) Long id, 
			@RequestBody(description = "Representação de uma forma de pagamento com os novos dados", required = true) FormaPagamentoInputDto formaPagamentoInput);

	@Operation(summary = "Exclui uma forma de pagamento por ID", responses = {
			@ApiResponse(responseCode = "204", description = "Forma de pagamento excluída"),
			@ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada", content = {@Content(schema = @Schema(ref = "Problema")) })
	})
	void delete(@Parameter(description = "ID de uma forma de pagamento", example = "1", required = true) Long id);

}