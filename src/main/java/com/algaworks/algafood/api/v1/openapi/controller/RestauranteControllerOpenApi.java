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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_oauth")
public interface RestauranteControllerOpenApi {

	@Operation(parameters = {
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

	RestauranteDto buscarId(Long id);

	RestauranteDto adicionar(RestauranteInputDto restauranteInput);

	RestauranteDto atualizar(Long id, RestauranteInputDto restauranteInput);

	void deletar(Long id);

	ResponseEntity<Void> ativar(Long restauranteId);

	void ativarMultiplos(List<Long> restauranteIds);

	void inativarMultiplos(List<Long> restauranteIds);

	ResponseEntity<Void> inativar(Long restauranteId);

	ResponseEntity<Void> fecharRestaurante(Long restauranteId);

	ResponseEntity<Void> abrirRestaurante(Long restauranteId);

}