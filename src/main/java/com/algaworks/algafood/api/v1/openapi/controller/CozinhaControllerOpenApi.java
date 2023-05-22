package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.algaworks.algafood.api.v1.model.CozinhaDto;
import com.algaworks.algafood.api.v1.model.input.CozinhaInputDto;
import com.algaworks.algafood.core.springdoc.PageableParameter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_oauth")
@Tag(name = "Cozinhas")
//@Tag(name = "Cozinhas", description = "Gerencia as cozinhas.") - As descricão está no SpringDocConfig
public interface CozinhaControllerOpenApi {

//	Annotation criada que contem os parametros "page", "size" e "sort"
//	Escondemos o objeto "Pageable" para que pegue os parametro da annotation
	
	@PageableParameter
	@Operation(summary = "Lista as cozinhas com paginação")
	PagedModel<CozinhaDto> listar(@Parameter(hidden = true) Pageable pageable);

	@Operation(summary = "Busca uma cozinha por ID", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID da cozinha inválido",content = @Content(schema = @Schema(ref = "Problema"))),
			@ApiResponse(responseCode = "404", description = "Cozinha não encontrada",content = @Content(schema = @Schema(ref = "Problema")))
	})
	CozinhaDto buscarId(Long cozinhaId);

	@Operation(summary = "Cadastra uma cozinha")
	CozinhaDto adicionar(@RequestBody(description = "Representação de uma nova cozinha", required = true) CozinhaInputDto cozinhaInputDto);

	@Operation(summary = "Atualiza uma cozinha por ID", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID da cozinha inválido",content = @Content(schema = @Schema(ref = "Problema"))),
			@ApiResponse(responseCode = "404", description = "Cozinha não encontrada",content = @Content(schema = @Schema(ref = "Problema")))
	})	
	CozinhaDto atualizar(Long cozinhaId, CozinhaInputDto cozinhaInput);

	@Operation(summary = "Exclui uma cozinha por ID", responses = {
			@ApiResponse(responseCode = "204"),
			@ApiResponse(responseCode = "404", description = "Cozinha não encontrada",content = @Content(schema = @Schema(ref = "Problema")))
	})
	void deletar(Long cozinhaId);

}