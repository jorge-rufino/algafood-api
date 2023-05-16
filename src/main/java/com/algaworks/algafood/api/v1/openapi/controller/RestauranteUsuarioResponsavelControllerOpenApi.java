package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.UsuarioDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_oauth")
public interface RestauranteUsuarioResponsavelControllerOpenApi {

	CollectionModel<UsuarioDto> listar(Long restauranteId);

	ResponseEntity<Void> associarResponsavel(Long restauranteId, Long usuarioId);

	ResponseEntity<Void> desassociarResponsavel(Long restauranteId, Long usuarioId);

}