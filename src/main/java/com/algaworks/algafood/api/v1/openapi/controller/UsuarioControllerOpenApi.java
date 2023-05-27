package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.UsuarioDto;
import com.algaworks.algafood.api.v1.model.input.SenhaInputDto;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInputDto;
import com.algaworks.algafood.api.v1.model.input.UsuarioInputDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_oauth")
@Tag(name = "Usuários")
public interface UsuarioControllerOpenApi {

	@Operation(summary = "Lista os usuários")
	CollectionModel<UsuarioDto> listar();

	@Operation(summary = "Busca um usuário por ID", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "400", description = "ID do usuário inválido", content = {@Content(schema = @Schema(ref = "Problema")) }),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) }),
	})
	UsuarioDto buscarPorId(
			@Parameter(description = "ID do usuário", example = "1", required = true) Long usuarioId);

	@Operation(summary = "Cadastra um usuário", responses = {
			@ApiResponse(responseCode = "201", description = "Usuário cadastrado"),
	})
	UsuarioDto adicionar(
			@RequestBody(description = "Representação de um novo usuário", required = true) UsuarioComSenhaInputDto usuarioInputComSenha);

	@Operation(summary = "Atualiza um usuário por ID", responses = {
			@ApiResponse(responseCode = "200", description = "Usuário atualizado"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
	})
	UsuarioDto atualizar(
			@Parameter(description = "ID do usuário", example = "1", required = true) Long usuarioId, 
			@RequestBody(description = "Representação de um usuário com os novos dados", required = true) UsuarioInputDto usuarioInput);

	@Operation(summary = "Atualiza a senha de um usuário", responses = {
			@ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
	})
	void alterarSenha(
			@Parameter(description = "ID do usuário", example = "1", required = true) Long usuarioId, 
			@RequestBody(description = "Representação de uma nova senha", required = true) SenhaInputDto senhaInput);

	@Operation(summary = "Exclui um usuário", responses = {
			@ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = {@Content(schema = @Schema(ref = "Problema")) })
	})
	void deletar(
			@Parameter(description = "ID do usuário", example = "1", required = true) Long usuarioId);
}