package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.algaworks.algafood.api.v1.model.CozinhaDto;
import com.algaworks.algafood.api.v1.model.input.CozinhaInputDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_oauth")
@Tag(name = "Cozinhas")
//@Tag(name = "Cozinhas", description = "Gerencia as cozinhas.") - As descricão está no SpringDocConfig
public interface CozinhaControllerOpenApi {

	PagedModel<CozinhaDto> listar(Pageable pageable);

	CozinhaDto buscarId(Long cozinhaId);

	CozinhaDto adicionar(CozinhaInputDto cozinhaInputDto);

	CozinhaDto atualizar(Long cozinhaId, CozinhaInputDto cozinhaInput);

	void deletar(Long cozinhaId);

}