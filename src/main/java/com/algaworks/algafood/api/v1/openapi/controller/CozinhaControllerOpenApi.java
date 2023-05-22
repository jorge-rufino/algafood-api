package com.algaworks.algafood.api.v1.openapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.algaworks.algafood.api.v1.model.CozinhaDto;
import com.algaworks.algafood.api.v1.model.input.CozinhaInputDto;
import com.algaworks.algafood.core.springdoc.PageableParameter;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_oauth")
@Tag(name = "Cozinhas")
//@Tag(name = "Cozinhas", description = "Gerencia as cozinhas.") - As descricão está no SpringDocConfig
public interface CozinhaControllerOpenApi {

//	Annotation criada que contem os parametros "page", "size" e "sort"
//	Escondemos o objeto "Pageable" para que pegue os parametro da annotation
	
	@PageableParameter	
	PagedModel<CozinhaDto> listar(@Parameter(hidden = true) Pageable pageable);

	CozinhaDto buscarId(Long cozinhaId);

	CozinhaDto adicionar(CozinhaInputDto cozinhaInputDto);

	CozinhaDto atualizar(Long cozinhaId, CozinhaInputDto cozinhaInput);

	void deletar(Long cozinhaId);

}