package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.PermissaoDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_oauth")
public interface GrupoPermissaoControllerOpenApi {

	CollectionModel<PermissaoDto> listar(Long grupoId);

	ResponseEntity<Void> desassociarPermissao(Long grupoId, Long permissaoId);

	ResponseEntity<Void> associarPermissao(Long grupoId, Long permissaoId);

}