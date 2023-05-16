package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;

import com.algaworks.algafood.api.v1.model.UsuarioDto;
import com.algaworks.algafood.api.v1.model.input.SenhaInputDto;
import com.algaworks.algafood.api.v1.model.input.UsuarioComSenhaInputDto;
import com.algaworks.algafood.api.v1.model.input.UsuarioInputDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_oauth")
public interface UsuarioControllerOpenApi {

	CollectionModel<UsuarioDto> listar();

	UsuarioDto buscarPorId(Long usuarioId);

	UsuarioDto adicionar(UsuarioComSenhaInputDto usuarioInputComSenha);

	UsuarioDto atualizar(Long usuarioId, UsuarioInputDto usuarioInput);

	void deletar(Long usuarioId);

	void alterarSenha(Long usuarioId, SenhaInputDto senhaInput);

}