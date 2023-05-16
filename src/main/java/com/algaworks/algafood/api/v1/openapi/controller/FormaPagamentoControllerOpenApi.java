package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import com.algaworks.algafood.api.v1.model.FormaPagamentoDto;
import com.algaworks.algafood.api.v1.model.input.FormaPagamentoInputDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_oauth")
public interface FormaPagamentoControllerOpenApi {

	//	Com DeepTags, o método só será executado completamente quando a "ETag" e "If-None-Match" forem diferentes
	ResponseEntity<CollectionModel<FormaPagamentoDto>> listar(ServletWebRequest request);

	ResponseEntity<FormaPagamentoDto> buscarId(Long id, ServletWebRequest request);

	FormaPagamentoDto adicionar(FormaPagamentoInputDto formaPagamentoInput);

	FormaPagamentoDto atualizar(Long id, FormaPagamentoInputDto formaPagamentoInput);

	void delete(Long id);

}