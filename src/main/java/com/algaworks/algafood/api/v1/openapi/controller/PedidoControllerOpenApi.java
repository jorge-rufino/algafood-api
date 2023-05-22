package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.algaworks.algafood.api.v1.model.PedidoDto;
import com.algaworks.algafood.api.v1.model.PedidoResumoDto;
import com.algaworks.algafood.api.v1.model.input.PedidoInputDto;
import com.algaworks.algafood.core.springdoc.PageableParameter;
import com.algaworks.algafood.domain.filter.PedidoFilter;

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
@Tag(name = "Pedidos")
public interface PedidoControllerOpenApi {

	@Operation(
			summary = "Pesquisa os pedidos",
			parameters = {
					@Parameter(in = ParameterIn.QUERY, name = "clienteId",
							description = "ID do cliente para filtro da pesquisa",
							example = "1", schema = @Schema(type = "integer")),
					@Parameter(in = ParameterIn.QUERY, name = "restauranteId",
							description = "ID do restaurante para filtro da pesquisa",
							example = "1", schema = @Schema(type = "integer")),
					@Parameter(in = ParameterIn.QUERY, name = "dataCriacaoInicio",
							description = "Data/hora de criação inicial para filtro da pesquisa",
							example = "2019-12-01T00:00:00Z", schema = @Schema(type = "string", format = "date-time")),
					@Parameter(in = ParameterIn.QUERY, name = "dataCriacaoFim",
							description = "Data/hora de criação final para filtro da pesquisa",
							example = "2019-12-02T23:59:59Z", schema = @Schema(type = "string", format = "date-time"))
			}
	)
	@PageableParameter
	PagedModel<PedidoResumoDto> pesquisar(
			@Parameter(hidden = true) PedidoFilter filter,
			@Parameter(hidden = true) Pageable pageable);

	@Operation(summary = "Busca um pedido por código", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = {@Content(schema = @Schema(ref = "Problema"))}),
	})
	PedidoDto buscarPorCodigo(
			@Parameter(description = "Código de um pedido", example = "04813f77-79b5-11ec-9a17-0242ac1b0002",required = true) String codigoPedido);

	@Operation(summary = "Registra um pedido", responses = {
			@ApiResponse(responseCode = "201", description = "Pedido registrado"),
	})
	PedidoDto emitir(@RequestBody(description = "Representação de um novo pedido", required = true) PedidoInputDto pedidoInput);

}