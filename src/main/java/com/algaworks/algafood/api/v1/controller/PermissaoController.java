package com.algaworks.algafood.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.assembler.PermissaoDtoAssembler;
import com.algaworks.algafood.api.v1.model.PermissaoDto;
import com.algaworks.algafood.domain.services.PermissaoService;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController {

	@Autowired
	private PermissaoService permissaoService;
	
	@Autowired
	private PermissaoDtoAssembler permissaoDtoAssembler;
	
	@GetMapping
	public CollectionModel<PermissaoDto> lista(){
		return permissaoDtoAssembler.toCollectionModel(permissaoService.listar());
	}
}
