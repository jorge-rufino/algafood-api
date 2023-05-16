package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.algaworks.algafood.api.v1.model.FormaPagamentoDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_oauth")
public interface RestauranteFormaPagamentoControllerOpenApi {

	CollectionModel<FormaPagamentoDto> listar(Long restauranteId);

	ResponseEntity<Void> desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId);

	ResponseEntity<Void> associarFormaPagamento(Long restauranteId, Long formaPagamentoId);

}